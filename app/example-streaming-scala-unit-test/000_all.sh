#!/bin/bash

set -e

# Ensure you are in this directory
cd $(dirname "$0")

$(git rev-parse --show-toplevel)/bin/mvn test
