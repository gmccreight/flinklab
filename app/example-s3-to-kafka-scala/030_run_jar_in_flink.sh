#!/bin/bash

set -e

docker-compose up -d jobmanager taskmanager

# Ensure you are in this directory
cd $(dirname "$0")

echo "Running program"
$(git rev-parse --show-toplevel)/bin/flink run \
                                --detached target/scala-2.11/s3-to-kafka-assembly-0.1.jar \
                                --sink-topic example-out \
                                --s3-key test/
