#!/bin/bash

set -e

./020_compile_jar.sh
./025_run_unit_tests.sh
./030_run_jar_in_flink.sh
./040_open_web_browser_to_dashboard.sh
