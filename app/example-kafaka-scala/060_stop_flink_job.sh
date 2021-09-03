#!/bin/bash

set -e

JOB_ID=$($(git rev-parse --show-toplevel)/bin/flink list -r | grep "Kafka Example" | cut -d" " -f 4)

$(git rev-parse --show-toplevel)/bin/flink cancel $JOB_ID
