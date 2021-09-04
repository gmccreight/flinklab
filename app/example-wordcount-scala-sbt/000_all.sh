#!/bin/bash

set -e

./020_compile_jar.sh
./030_run_jar_in_flink.sh
