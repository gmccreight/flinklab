package org.flinklab.s3_to_kafka

import org.apache.flink.api.java.utils.ParameterTool
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment
import org.apache.flink.streaming.api.functions.source.FileProcessingMode
import org.apache.flink.api.common.io.GenericCsvInputFormat
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer
import org.apache.flink.streaming.util.serialization.SimpleStringSchema
import org.flinklab.util.{Parameters, KafkaProperties}

class SingleStringCSVInputFormat extends GenericCsvInputFormat[String] {
  override def readRecord(
      reuse: String,
      bytes: Array[Byte],
      offset: Int,
      numBytes: Int
  ): String = {
    bytes.slice(offset, numBytes + offset).map(_.toChar).mkString("")
  }
}

object S3ToKafka {
  def main(args: Array[String]): Unit = {
    val csvFormat = new SingleStringCSVInputFormat()

    val params = new Parameters(args)
    val (_, producerProps) = KafkaProperties.get(params)

    val schema = new SimpleStringSchema
    val kafkaProducer = new FlinkKafkaProducer[String](
      params.expect("sink-topic"),
      schema,
      producerProps
    )

    val key = params.getOrExpectEnv("s3-key")("S3_KEY")
    val bucket = params.getOrExpectEnv("s3-bucket")("S3_BUCKET")
    println(s"Watching s3://${bucket}/${key}")

    val env = StreamExecutionEnvironment.getExecutionEnvironment();
    val rows = env.readFile(
      csvFormat,
      s"s3://${bucket}/${key}",
      FileProcessingMode.PROCESS_CONTINUOUSLY,
      1000 // Polling interval (ms)
    )
    rows.addSink(kafkaProducer)
    env.execute("S3 to Kafka")
  }
}
