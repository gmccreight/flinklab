#!/bin/bash

set -e

JOB_ID=$($(git rev-parse --show-toplevel)/bin/flink list -r | grep "S3 to S3" | cut -d" " -f 4)

$(git rev-parse --show-toplevel)/bin/flink cancel $JOB_ID
