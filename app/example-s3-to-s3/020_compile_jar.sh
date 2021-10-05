#!/bin/bash

set -e

# Ensure you are in the project dir
cd $(dirname "$0")

echo "Compiling package"
$(git rev-parse --show-toplevel)/bin/sbt assembly
