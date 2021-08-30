#!/bin/bash

set -e

echo "Cleaning target"
$(git rev-parse --show-toplevel)/bin/mvn clean
echo "Compiling package"
$(git rev-parse --show-toplevel)/bin/mvn package app/example-wordcount-scala-with-unit-test/wc
