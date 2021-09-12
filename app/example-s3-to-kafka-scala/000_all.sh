#!/bin/bash

set -e

# Ensure you are in the project dir
cd $(dirname "$0")

./010_set_up_kafka_topic_and_bucket.sh
./020_compile_jar.sh
./030_run_jar_in_flink.sh
./040_read_kafka_topic.sh 
./060_stop_flink_job.sh
