#!/bin/bash

set -e

docker-compose up -d jobmanager taskmanager

# Ensure you are in this directory
cd $(dirname "$0")

echo "Running program"
$(git rev-parse --show-toplevel)/bin/flink run \
                                --detached kafka_example/target/kafka_example-0.1.jar \
                                --source-topic example-in \
                                --sink-topic example-out \
                                --broker-list kafka:9092
