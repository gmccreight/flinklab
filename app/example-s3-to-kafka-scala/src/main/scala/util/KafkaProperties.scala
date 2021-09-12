package org.flinklab.util

import java.util.Properties

object KafkaProperties {
  def get(
      params: Parameters
  ): (Properties, Properties) = {
    val bootstrapServers =
      params.getOrEnvOrElse("kafka-brokers")("KAFKA_BROKERS")("kafka:9092")

    val consumerProps = new Properties
    consumerProps.setProperty("bootstrap.servers", bootstrapServers)
    // Since this group ID is created when the job is created, it'll be the same    // Since this group ID is created when the job is created, it'll be the same
    // across all tasks in the job.
    consumerProps.setProperty(
      "group.id",
      s"flink-kafka-test-${System.currentTimeMillis}"
    )

    // Producer properties: we just need the broker list
    val producerProps = new Properties
    producerProps.setProperty("bootstrap.servers", bootstrapServers)

    // If the user specified the --secure option as true, enable it by setting the security.protocol
    // property to SASL_PLAINTEXT for both the consumer and the producer
    if (params.get("secure").equals("true")) {
      consumerProps.setProperty("security.protocol", "SASL_PLAINTEXT")
      producerProps.setProperty("security.protocol", "SASL_PLAINTEXT")
    }

    // Return the properties as a pair
    (consumerProps, producerProps)
  }
}
