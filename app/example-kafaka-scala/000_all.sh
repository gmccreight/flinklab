#!/bin/bash

set -e

./010_set_up_kafka_topics.sh
./020_compile_jar.sh
./030_run_jar_in_flink.sh
./040_open_web_browser_to_dashboard.sh
./050_insert_data_into_stream_and_monitor.sh
./060_stop_flink_job.sh
