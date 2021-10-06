#!/bin/bash

set -e

# Ensure you are in the project dir
cd $(dirname "$0")

set +e
# ok to fail if they don't exist
./005_clean_up_the_bucket.sh
set -e

./010_set_up_bucket.sh
./020_compile_jar.sh
./030_run_jar_in_flink.sh
./999_read_output_in_bucket.sh
./035_sync_some_more_data_into_s3.sh
./999_read_output_in_bucket.sh
./060_stop_flink_job.sh
./065_read_single_line_of_last_file.sh
