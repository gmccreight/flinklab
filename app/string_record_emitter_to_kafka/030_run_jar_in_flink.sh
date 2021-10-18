#!/bin/bash

set -e

docker-compose up -d jobmanager taskmanager

# Move to this dir
cd $(dirname "$0")

echo "Running program"
$(git rev-parse --show-toplevel)/bin/flink run -d target/scala-2.12/string-record-emitter-to-kafka-assembly-0.1.jar
