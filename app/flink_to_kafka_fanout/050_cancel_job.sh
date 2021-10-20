#!/bin/bash

set -e

JOB_ID=$($(git rev-parse --show-toplevel)/bin/flink list -r | grep "String Record Emitter To Kafka" | cut -d" " -f 4)

$(git rev-parse --show-toplevel)/bin/flink cancel $JOB_ID
