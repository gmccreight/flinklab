package org.flinklab

import org.apache.flink.streaming.api.scala._

import java.util.Calendar
import org.apache.flink.streaming.api.functions.source.RichParallelSourceFunction
import org.apache.flink.streaming.api.functions.source.SourceFunction.SourceContext
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer
import org.apache.flink.streaming.util.serialization.SimpleStringSchema

import java.util.Properties

class StringRecordEmitterSource extends RichParallelSourceFunction[String] {

  var isRunning: Boolean = true

  override def run(srcCtx: SourceContext[String]): Unit = {

    val taskIdx = this.getRuntimeContext.getIndexOfThisSubtask

    while (isRunning) {
      val curTime = Calendar.getInstance.getTimeInMillis
      srcCtx.collect(s"string_record_$curTime")
      Thread.sleep(1000)
    }

  }

  override def cancel(): Unit = {
    isRunning = false
  }

}

object StringRecordEmitterToKafka {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    val source = new StringRecordEmitterSource()
    val dataStream: DataStream[String] = env.addSource(source)

    val producerProps = new Properties
    producerProps.setProperty("bootstrap.servers", "kafka:9092")

    val schema = new SimpleStringSchema
    val kafkaProducer = new FlinkKafkaProducer[String](
      "string-record-emitter-out",
      schema,
      producerProps
    )

    dataStream.addSink(kafkaProducer)
    env.execute("String Record Emitter To Kafka")

  }
}
