#!/bin/bash

set -e

clear

./010_flinklabqs_create_or_recreate_job.sh
./020_flinklabqs_compile_jar.sh
./030_flinklabqs_run_jar.sh
exit
