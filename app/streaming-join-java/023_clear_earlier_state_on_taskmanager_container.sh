#!/bin/bash

set -e

THIS_DIR="$(cd "$(dirname "$0")"; pwd -P)"

docker exec -ti flinklab_taskmanager_1 sh -c "rm -f /tmp/total"
