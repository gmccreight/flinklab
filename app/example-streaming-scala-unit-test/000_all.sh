#!/bin/bash

set -e

$(git rev-parse --show-toplevel)/bin/mvn test app/example-streaming-scala-unit-test
