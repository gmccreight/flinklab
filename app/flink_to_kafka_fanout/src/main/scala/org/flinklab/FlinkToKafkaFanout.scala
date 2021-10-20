package org.flinklab

import org.apache.flink.streaming.api.scala._

import java.util.Calendar
import org.apache.flink.streaming.api.functions.source.RichParallelSourceFunction
import org.apache.flink.streaming.api.functions.source.SourceFunction.SourceContext
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer
import org.apache.flink.streaming.util.serialization.SimpleStringSchema
import org.apache.flink.streaming.connectors.kafka.partitioner.FlinkKafkaPartitioner

import java.util.Optional
import java.util.Properties

class StringRecordEmitterSource extends RichParallelSourceFunction[String] {

  var isRunning: Boolean = true

  override def run(srcCtx: SourceContext[String]): Unit = {

    val taskIdx = this.getRuntimeContext.getIndexOfThisSubtask

    while (isRunning) {
      val curTime = Calendar.getInstance.getTimeInMillis
      srcCtx.collect(s"string_record_$curTime")
      Thread.sleep(10)
    }

  }

  override def cancel(): Unit = {
    isRunning = false
  }

}

object FlinkToKafkaFanout {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    val source = new StringRecordEmitterSource()
    val dataStream: DataStream[String] = env.addSource(source)

    val producerProps = new Properties
    producerProps.setProperty("bootstrap.servers", "kafka:9092")

    val schema = new SimpleStringSchema
    val emptyPartitionerToCauseRoundRobin: Optional[FlinkKafkaPartitioner[String]] = Optional.empty()
    val kafkaProducer = new FlinkKafkaProducer[String](
      "flink-to-kafka-fanout",
      schema,
      producerProps,
      emptyPartitionerToCauseRoundRobin
    )

    dataStream
      .addSink(kafkaProducer)
      .setParallelism(2)
    env.execute("Flink to Kafka Fanout")

  }
}
