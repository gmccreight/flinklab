#!/bin/bash

set -e

THIS_DIR="$(cd "$(dirname "$0")"; pwd -P)"

# We want to have this data available in /tmp/transactions
docker exec -ti flinklab_taskmanager_1 sh -c "mkdir -p /tmp/transactions"
docker cp $THIS_DIR/transactions/1 flinklab_taskmanager_1:/tmp/transactions/1
