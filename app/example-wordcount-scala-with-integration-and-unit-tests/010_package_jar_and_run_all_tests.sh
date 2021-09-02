#!/bin/bash

set -e

echo "Cleaning target"
$(git rev-parse --show-toplevel)/bin/mvn clean
echo "Compiling package - also runs integration and unit tests"
$(git rev-parse --show-toplevel)/bin/mvn package app/example-wordcount-scala-with-integration-and-unit-tests/wc
