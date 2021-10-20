### Flink to Kafka Fanout

NOTE: the CHECK does not work, because this does not behave the way we want it
to.  Rather it sticks when checking partition 2 (the third partition), which is
one greater than Flink's parallelism.

You should be able to run flink at a lower parallelism than the destination
Kafka topic you will be outputting to.  The hope would be that the partition
assignment would be distributed evenly.

We create a topic, flink-to-kafka-fanout, with 6 partitions, then set a
parallelism of 2 in the Flink job.

To run it, just run ./000_all.sh
