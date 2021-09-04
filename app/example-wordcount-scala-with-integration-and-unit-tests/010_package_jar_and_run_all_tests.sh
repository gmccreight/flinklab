#!/bin/bash

set -e

# Ensure you are in the project dir
cd $(dirname "$0")/wc

echo "Cleaning target"
$(git rev-parse --show-toplevel)/bin/mvn clean
echo "Compiling package - also runs integration and unit tests"
$(git rev-parse --show-toplevel)/bin/mvn package
