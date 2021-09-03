package org.flinklab.sic

import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.JsonNode
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper
import org.apache.flink.streaming.api.functions.source.SourceFunction
import org.apache.flink.streaming.api.functions.source.SourceFunction.SourceContext
import org.apache.flink.streaming.api.watermark.Watermark

import java.io._
import java.sql.Timestamp
import java.util.Arrays

class TransactionsSource(private val dataFilePath: String,
                         private val watermarkFrequency: Int)
    extends SourceFunction[JsonNode] {

  @transient private var reader: BufferedReader = _

  override def run(sourceContext: SourceContext[JsonNode]): Unit = {
    val directory: File = new File(dataFilePath)
    val files: Array[java.io.File] = directory.listFiles()

    for (file <- files.sorted) {
      val fis: FileInputStream = new FileInputStream(file)
      reader = new BufferedReader(new InputStreamReader(fis, "UTF-8"))
      generateStream(sourceContext)
      this.reader.close()
      this.reader = null
    }
  }

  private def generateStream(sourceContext: SourceContext[JsonNode]): Unit = {
    var numEvents: Int = 0
    var line: String = null
    var event: JsonNode = null
    val mapper: ObjectMapper = new ObjectMapper()
    while (reader.ready() && Option(line = reader.readLine()) != None) {
      { numEvents += 1; numEvents - 1 }
      event = mapper.readTree(line)
      val timestamp: Long =
        Timestamp.valueOf(event.get("ts").textValue()).getTime
      sourceContext.collectWithTimestamp(event, timestamp)

      // generate watermark
      if (numEvents == watermarkFrequency) {
        val nextWatermark: Watermark = new Watermark(timestamp - 5000)
        sourceContext.emitWatermark(nextWatermark)
        numEvents = 0
      }
    }
  }

  override def cancel(): Unit = {
    try if (this.reader != null) {
      this.reader.close()
    } catch {
      case ioe: IOException =>
        throw new RuntimeException("Could not cancel SourceFunction", ioe)

    } finally this.reader = null
  }

}
