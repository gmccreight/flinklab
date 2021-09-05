#!/bin/bash

set -e

./020_compile_jar.sh
./025_run_integration_tests.sh
./030_run_jar_in_flink.sh
