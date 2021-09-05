#!/bin/bash

set -e

docker-compose up -d jobmanager taskmanager

# Move to this dir
cd $(dirname "$0")

echo "Running program"
$(git rev-parse --show-toplevel)/bin/flink run \
                                target/scala-2.11/example-wordcount-scala-w-tests-sbt-assembly-0.1.jar
