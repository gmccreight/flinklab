package org.flinklab.s3_to_s3

import org.apache.flink.api.java.utils.ParameterTool
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment
import org.apache.flink.streaming.api.functions.source.FileProcessingMode
import org.apache.flink.api.common.io.GenericCsvInputFormat
import org.apache.flink.streaming.util.serialization.SimpleStringSchema
import org.flinklab.util.Parameters

import org.apache.flink.core.fs.Path
import org.apache.flink.streaming.api.functions.sink.filesystem.{StreamingFileSink, OutputFileConfig}
import org.apache.flink.api.common.serialization.SimpleStringEncoder
import org.apache.flink.streaming.api.functions.sink.filesystem.rollingpolicies.DefaultRollingPolicy


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

object S3ToS3 {
  def main(args: Array[String]): Unit = {
    val csvFormat = new SingleStringCSVInputFormat()

    val params = new Parameters(args)

    val bucket = params.getOrExpectEnv("s3-bucket")("S3_BUCKET")
    val key_in = params.getOrExpectEnv("s3-key-in")("S3_KEY_IN")
    val key_out = params.getOrExpectEnv("s3-key-out")("S3_KEY_OUT")

    println(s"Watching s3://${bucket}/${key_in}")

    val env = StreamExecutionEnvironment.getExecutionEnvironment();

    // Checkpointing is necessary to cause the in-progress .part files in the
    // output into finalized part files
    env.enableCheckpointing(10000);
 
    val rows = env.readFile(
      csvFormat,
      s"s3://${bucket}/${key_in}",
      FileProcessingMode.PROCESS_CONTINUOUSLY,
      1000 // Polling interval (ms)
    )

    val s3Sink = StreamingFileSink.forRowFormat(new Path( s"s3://${bucket}/${key_out}"), new SimpleStringEncoder[String]())
      .withRollingPolicy(
        DefaultRollingPolicy.builder()
            .withRolloverInterval(1000L)
            .withInactivityInterval(1000L)

            // This needs to be small enough that our small amount of test data
            // actually causes part files to be finalized
            .withMaxPartSize(1024) // 1k - tag_scaling
            .build())
      .build()

    rows.addSink(s3Sink)
    
    env.execute("S3 to S3")
  }
}
