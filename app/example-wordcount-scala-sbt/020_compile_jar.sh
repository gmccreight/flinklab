#!/bin/bash

set -e

# Move to this dir
cd $(dirname "$0")

echo "Cleaning target"
$(git rev-parse --show-toplevel)/bin/sbt clean
echo "Compiling package"
$(git rev-parse --show-toplevel)/bin/sbt assembly
