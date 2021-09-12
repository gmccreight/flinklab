package org.flinklab

import org.flinklab.util.{ThingUpdate, ThingSource, ThingTimeAssigner}
import org.flinklab.util.{NormalizationUpdate, NormalizationSource, NormalizationTimeAssigner}

import org.apache.flink.api.common.state.{MapStateDescriptor, ValueState, ValueStateDescriptor}
import org.apache.flink.api.scala._
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.datastream.BroadcastStream
import org.apache.flink.streaming.api.functions.co.KeyedBroadcastProcessFunction
import org.apache.flink.streaming.api.scala.{DataStream, KeyedStream, StreamExecutionEnvironment}
import org.apache.flink.util.Collector

object ValueNormalization {

  def main(args: Array[String]) {

    val env = StreamExecutionEnvironment.getExecutionEnvironment

    env.getCheckpointConfig.setCheckpointInterval(10 * 1000)

    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)

    env.getConfig.setAutoWatermarkInterval(1000L)

    val thingData: DataStream[ThingUpdate] = env
      .addSource(new ThingSource)
      .assignTimestampsAndWatermarks(new ThingTimeAssigner)

    val normalizationData: DataStream[NormalizationUpdate] = env
      .addSource(new NormalizationSource)
      .assignTimestampsAndWatermarks(new NormalizationTimeAssigner)

    val keyedThingData: KeyedStream[ThingUpdate, String] = thingData.keyBy(_.id)

    val broadcastStateDescriptor =
      new MapStateDescriptor[Long, Long]("normalizationData", classOf[Long], classOf[Long])

    val broadcastNormalizationUpdates: BroadcastStream[NormalizationUpdate] = normalizationData
      .broadcast(broadcastStateDescriptor)

    val changes: DataStream[(String, Long, Long, Boolean, Boolean)] = keyedThingData
      .connect(broadcastNormalizationUpdates)
      .process(new UpdatableOutputValuesChangedFunction())

    changes.writeAsText("/tmp/evn_changes", org.apache.flink.core.fs.FileSystem.WriteMode.OVERWRITE)

    env.execute("Generate Normalization Changes")
  }
}

class UpdatableOutputValuesChangedFunction()
    extends KeyedBroadcastProcessFunction[String, ThingUpdate, NormalizationUpdate, (String, Long, Long, Boolean, Boolean)] {

  private lazy val normalizationStateDescriptor =
    new MapStateDescriptor[Long, Long]("normalizationData", classOf[Long], classOf[Long])

  private var lastNormalizedIdStateHandle: ValueState[Long] = _

  override def open(parameters: Configuration): Unit = {
    val lastNormalizedIdDescriptor = new ValueStateDescriptor[Long]("lastNormalizedId", classOf[Long])
    lastNormalizedIdStateHandle = getRuntimeContext.getState[Long](lastNormalizedIdDescriptor)
  }

  override def processBroadcastElement(
      update: NormalizationUpdate,
      ctx: KeyedBroadcastProcessFunction[String, ThingUpdate, NormalizationUpdate, (String, Long, Long, Boolean, Boolean)]#Context,
      out: Collector[(String, Long, Long, Boolean, Boolean)]): Unit = {

    val normalizationDataStateHandle = ctx.getBroadcastState(normalizationStateDescriptor)

    val currentNormalizedId: Long = normalizationDataStateHandle.get(update.id)

    if (update.normalizedId != 0) {
      // configure a new normalization
      normalizationDataStateHandle.put(update.id, update.normalizedId)
    } else {
      // remove the normalization
      normalizationDataStateHandle.remove(update.id)
    }

    if (update.normalizedId != currentNormalizedId) {
      // TODO: update all the things that have foreignId that is equal to update.id
      // out.collect((thingUpdate.id, thingUpdate.foreignId, normalizedId))
    }

  }

  override def processElement(
      thingUpdate: ThingUpdate,
      readOnlyCtx: KeyedBroadcastProcessFunction[String, ThingUpdate, NormalizationUpdate, (String, Long, Long, Boolean, Boolean)]#ReadOnlyContext,
      out: Collector[(String, Long, Long, Boolean, Boolean)]): Unit = {

    val normalizationDataStateHandle = readOnlyCtx.getBroadcastState(normalizationStateDescriptor)

    var metadata_HasNormalizationValue = false

    if (normalizationDataStateHandle.contains(thingUpdate.foreignId)) {
      var metadata_HasNormalizationValue = true

      val normalizedId: Long = normalizationDataStateHandle.get(thingUpdate.foreignId)

      val lastNormalizedId = lastNormalizedIdStateHandle.value()

      var metadata_isChange = false

      if (normalizedId != lastNormalizedId) {
        this.lastNormalizedIdStateHandle.update(normalizedId)
        metadata_isChange = true
      }

      // Output, even if not change... just to make it easier to debug
      out.collect((thingUpdate.id, thingUpdate.foreignId, normalizedId, metadata_HasNormalizationValue, metadata_isChange))
    }
    else {
      // No normalization... just store the un-normalized value as the normalized value
      this.lastNormalizedIdStateHandle.update(thingUpdate.foreignId)
      val metadata_isChange = false
      out.collect((thingUpdate.id, thingUpdate.foreignId, thingUpdate.foreignId, metadata_HasNormalizationValue, metadata_isChange))
    }
  }
}
