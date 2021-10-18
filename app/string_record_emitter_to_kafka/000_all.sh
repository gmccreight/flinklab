#!/bin/bash

set -e

./010_set_up_kafka_topic.sh
./020_compile_jar.sh
./030_run_jar_in_flink.sh
./040_check.sh
./050_cancel_job.sh
