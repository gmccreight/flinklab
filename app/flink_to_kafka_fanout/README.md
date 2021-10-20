### Flink to Kafka Fanout

Our conjecture is that you should be able to run Flink at a lower parallelism
than the destination Kafka topic you are outputting to, and that the messages
will be evenly distributed across the greater number of partitions.

To test that, we create a topic, flink-to-kafka-fanout, with 6 partitions, then
set a parallelism of 2 in the Flink job.

We run it using ./000_all.sh

### Fixing

    // added this line
    val emptyPartitionerToCauseRoundRobin: Optional[FlinkKafkaPartitioner[String]] = Optional.empty()

    val kafkaProducer = new FlinkKafkaProducer[String](
      "flink-to-kafka-fanout",
      schema,
      producerProps,
      emptyPartitionerToCauseRoundRobin // Then used it here
    )


### Results

Before the fix the CHECK did not work.  Rather, it got stuck when checking partition 2 (the
third partition), which is one greater than Flink's parallelism.

    CHECK: should distribute to all topic partitions
    CHECK 10 messages from partition 0 of 0..5
    string_record_1634762383013
    string_record_1634762383034
    string_record_1634762383055
    string_record_1634762383075
    string_record_1634762383096
    string_record_1634762383116
    string_record_1634762383137
    string_record_1634762383157
    string_record_1634762383178
    string_record_1634762383198
    Processed a total of 10 messages
    CHECK 10 messages from partition 1 of 0..5
    string_record_1634762387251
    string_record_1634762387272
    string_record_1634762387293
    string_record_1634762387313
    string_record_1634762387334
    string_record_1634762387354
    string_record_1634762387375
    string_record_1634762387395
    string_record_1634762387416
    string_record_1634762387437
    Processed a total of 10 messages
    CHECK 10 messages from partition 2 of 0..5

At this point it hangs.  If you set Flink's parallelism to 1, it hangs after
the checking the first partition (partition 0).
