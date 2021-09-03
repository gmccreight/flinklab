#!/bin/bash

set -e

docker-compose up -d jobmanager taskmanager

echo "Running program"
$(git rev-parse --show-toplevel)/bin/flink run \
                                --detached target/kafka_example-0.1.jar \
                                --source-topic example-in \
                                --sink-topic example-out \
                                --broker-list kafka:9092
