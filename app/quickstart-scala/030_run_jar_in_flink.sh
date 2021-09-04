#!/bin/bash

set -e

docker-compose up -d jobmanager taskmanager

# Ensure you are in this directory
cd $(dirname "$0")

echo "Running program"
$(git rev-parse --show-toplevel)/bin/flink run flinklabqs/target/flinklabqs-0.1.jar
