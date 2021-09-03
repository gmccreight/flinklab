package org.flinklab.examples

import java.util.Properties

import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.api.java.utils.ParameterTool
import org.apache.flink.api.scala._
import org.apache.flink.streaming.connectors.kafka.{FlinkKafkaConsumer, FlinkKafkaProducer}
import org.apache.flink.streaming.util.serialization.SimpleStringSchema

/**
  * Implements a basic Kafka consumer/producer
  * Adapted from: https://github.com/radicalbit/rbd-examples/blob/master/flink/kafka-connector-example/src/main/scala/io/radicalbit/flink/examples/KafkaConnectorExample.scala#L21
  *
  *  Can be run with e.g.:
  *  ./bin/flink run --detached target/kafka_example-0.1.jar --source-topic TopicIn --sink-topic TopicOut --broker-list kafka:9092
  *
  *  These parameters expected are:
  *  --broker-list (defaults to "kafka:9092"):  a comma-separated list of <host:port> pairs pointing to Kafka brokers
  *  --source-topic (required): the Kafkatopic we'll read from
  *  --sink-topic (required):   the Kafka topic we'll write to
  *  --secure:       true if running on a secure cluster (Kerberos), otherwise false
  *
  */
object KafkaExample {

  def main(args: Array[String]) {
    val params: ParameterTool = ParameterTool.fromArgs(args)

    // set up execution environment
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    val schema = new SimpleStringSchema

    // make parameters available in the web interface
    env.getConfig.setGlobalJobParameters(params)

    // Translate input parameters into Kafka-friendly properties
    val (consumerProps, producerProps) = getConsumerAndProducerProps(params)

    // Define the Kafka consumer and producer for Flink using the given parameters
    val kafkaConsumer = new FlinkKafkaConsumer[String](params.get("source-topic"), schema, consumerProps)
    val kafkaProducer = new FlinkKafkaProducer[String](params.get("sink-topic"), schema, producerProps)

    // Our simple job: append a [processed] tag to each incoming string and write it to the sink
    env.addSource(kafkaConsumer).map(in => s"$in [processed]").addSink(kafkaProducer)

    // Run the job
    env.execute("Kafka Example")
  }

  private def getConsumerAndProducerProps(params: ParameterTool): (Properties, Properties) = {

    // Consumer properties: put together the broker list and a unique group id
    // Since this group ID is created when the job is created, it'll be the same
    // across all tasks in the job.
    val bootstrapServers = if (params.has("broker-list")) {
      params.get("broker-list")
    } else {
      "kafka:9092"
    }
    val consumerProps = new Properties
    consumerProps.setProperty("bootstrap.servers", bootstrapServers)
    consumerProps.setProperty("group.id", s"flink-kafka-test-${System.currentTimeMillis}")

    // Producer properties: we just need the broker list
    val producerProps = new Properties
    producerProps.setProperty("bootstrap.servers", bootstrapServers)

    // If the user specified the --secure option as true, enable it by setting the security.protocol
    // property to SASL_PLAINTEXT for both the consumer and the producer
    if (params.has("secure") && params.get("secure").equals("true")) {
      consumerProps.setProperty("security.protocol", "SASL_PLAINTEXT")
      producerProps.setProperty("security.protocol", "SASL_PLAINTEXT")
    }

    // Return the properties as a pair
    (consumerProps, producerProps)
  }

}
