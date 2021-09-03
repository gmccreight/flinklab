#!/bin/bash

set -e

docker-compose up -d jobmanager taskmanager

echo "Running program"
$(git rev-parse --show-toplevel)/bin/flink run target/wc-0.1.jar