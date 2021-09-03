package org.flinklab.sic

import org.apache.flink.api.common.serialization.DeserializationSchema
import org.apache.flink.api.common.typeinfo.TypeInformation
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.JsonNode
import java.io.IOException
import JsonDeserializationSchema._

object JsonDeserializationSchema {

  private val objectMapper: ObjectMapper = new ObjectMapper()

}

@SerialVersionUID(1L)
class JsonDeserializationSchema extends DeserializationSchema[JsonNode] {

  override def deserialize(message: Array[Byte]): JsonNode =
    objectMapper.readTree(message)

  override def isEndOfStream(nextElement: JsonNode): Boolean = false

  override def getProducedType(): TypeInformation[JsonNode] =
    TypeInformation.of(classOf[JsonNode])

}

