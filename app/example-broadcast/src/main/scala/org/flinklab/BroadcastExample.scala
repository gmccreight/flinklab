package org.flinklab

import org.apache.flink.api.scala._
import org.apache.flink.api.common.state.{MapStateDescriptor, ValueState, ValueStateDescriptor}
import org.apache.flink.api.common.typeinfo.BasicTypeInfo
import org.apache.flink.runtime.state.KeyedStateFunction
import org.apache.flink.streaming.api.functions.co.KeyedBroadcastProcessFunction
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.util.Collector

object BroadcastExample {

  def main(args: Array[String]): Unit = {

    val keyedInput = List[(Int, Int)](

    // keyed by the ._1 element, which is this one
    // |
      (1, 1),
      (1, 5),

      (2, 2),
      (2, 6),

      (3, 3),
      (3, 7),

      (4, 4),
      (4, 8)
    )

    // These broadcastInput numbers are different from the keyedInput numbers
    // so their relationship to the keyedInput numbers is confusing.
    // They are, basically, not related in any way.
    // You could imagine that in a real application, you might want them to
    // be related (like you'd like to *use* the broadcast mapping in some way
    // to affect the processed element), but that's not the case here, so don't be
    // led astray!
    //
    // In the processBroadcastElement each of these numbers is turned into a
    // map when the state is "put" (saved).  That map looks like this:
    // 70 -> 700
    // 80 -> 800
    // etc
    val broadcastInput = List(70, 80, 90)

    val env = StreamExecutionEnvironment.getExecutionEnvironment

    val mapStateDescriptor = new MapStateDescriptor[String, Integer](
      "Broadcast", BasicTypeInfo.STRING_TYPE_INFO, BasicTypeInfo.INT_TYPE_INFO)

    val elementStream = env
      .fromCollection(keyedInput)
      .rebalance
      .map(value => value)
      .setParallelism(2)
      .keyBy(value => value._1)

    val broadcastStream = env
      .fromCollection(broadcastInput)
      .flatMap((value: Int, out: Collector[Int]) => out.collect(value))
      .setParallelism(2)
      .broadcast(mapStateDescriptor)

    val output = elementStream
      .connect(broadcastStream)
      .process(new KeyedBroadcastProcessFunction[Int, (Int, Int), Int, String]() {

        private lazy val valueStateDesc = new ValueStateDescriptor[String](
          "any", BasicTypeInfo.STRING_TYPE_INFO)

        private lazy val mapStateDesc = new MapStateDescriptor[String, Integer](
          "Broadcast", BasicTypeInfo.STRING_TYPE_INFO, BasicTypeInfo.INT_TYPE_INFO)

        @throws[Exception]
        override def processBroadcastElement(
            value: Int,
            ctx: KeyedBroadcastProcessFunction[Int, (Int, Int), Int, String]#Context,
            out: Collector[String])
          : Unit = {

          // Turns 70 into a mapping 70 -> 700
          ctx.getBroadcastState(mapStateDesc).put((value) + "", value * 10)

          ctx.applyToKeyedState(valueStateDesc, new KeyedStateFunction[Int, ValueState[String]] {
            override def process(key: Int, state: ValueState[String]): Unit =
              out.collect("Broadcast taskIndex:" +
                getRuntimeContext.getIndexOfThisSubtask + ", key:" + key + ", state.value:" + state.value)
          })
        }

        @throws[Exception]
        override def processElement(
            value: (Int, Int),
            ctx: KeyedBroadcastProcessFunction[Int, (Int, Int), Int, String]#ReadOnlyContext,
            out: Collector[String])
          : Unit = {

          val prev = getRuntimeContext.getState(valueStateDesc).value

          val str = new StringBuilder
          str.append("Value=").append(value).append(" Broadcast State=[")

          import scala.collection.JavaConversions._
          for (entry <- ctx.getBroadcastState(mapStateDesc).immutableEntries()) {
            str.append(entry.getKey).append("->").append(entry.getValue).append(" ")
          }
          str.append("]")

          getRuntimeContext.getState(valueStateDesc).update(str.toString)

          out.collect("BEFORE: " + prev + " " + "AFTER: " + str)

        }
      })

    output.writeAsText("/tmp/eb_output", org.apache.flink.core.fs.FileSystem.WriteMode.OVERWRITE)
    env.execute
  }
}
