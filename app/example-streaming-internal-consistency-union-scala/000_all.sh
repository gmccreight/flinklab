#!/bin/bash

set -e

./020_compile_jar.sh
./023_clear_earlier_output_state_on_taskmanager_container.sh
./025_copy_transaction_data_to_taskmanager_container.sh
./030_run_jar_in_flink.sh
sleep 1
./035_verify_the_total.sh
./040_open_web_browser_to_dashboard.sh
