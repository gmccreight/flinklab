#!/bin/bash

set -e

docker-compose up -d jobmanager taskmanager

# Ensure you are in this directory
cd $(dirname "$0")

echo "Running program"
$(git rev-parse --show-toplevel)/bin/flink run \
                                --detached target/scala-2.11/s3-to-s3-assembly-0.1.jar \
                                --s3-key-in test/ \
                                --s3-key-out test-out/
