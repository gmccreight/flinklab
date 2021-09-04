#!/bin/bash

set -e

# Ensure you are in the project dir
cd $(dirname "$0")/kafka_example

echo "Cleaning target"
$(git rev-parse --show-toplevel)/bin/mvn clean
echo "Compiling package"
$(git rev-parse --show-toplevel)/bin/mvn package
