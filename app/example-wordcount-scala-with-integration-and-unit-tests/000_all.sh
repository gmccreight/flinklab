#!/bin/bash

set -e

./010_package_jar_and_run_all_tests.sh
./020_run_jar_in_flink.sh
./030_open_web_browser_to_dashboard.sh
