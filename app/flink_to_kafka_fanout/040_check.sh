#!/bin/bash

set -e

echo CHECK: should distribute to all topic partitions

for i in `seq 0 5`; do
  echo CHECK 10 messages from partition $i of 0..5
  $(git rev-parse --show-toplevel)/bin/kafka consumer --topic flink-to-kafka-fanout --offset latest --partition $i --max-messages 10
done
