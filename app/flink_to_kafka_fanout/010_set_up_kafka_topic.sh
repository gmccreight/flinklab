#!/bin/bash

set -e

# Ensure kafka container is up-and-running
docker compose up -d kafka

$(git rev-parse --show-toplevel)/bin/kafka topics --delete --topic flink-to-kafka-fanout --if-exists

# Hey, you seeing a problem where the topic exists (where it looks like the
# delete above failed?) If so, it's possible that your Flink job is still
# running, producing records into the topic, in which case the above delete
# will not do anything.
$(git rev-parse --show-toplevel)/bin/kafka topics --create --topic flink-to-kafka-fanout --partitions 6
