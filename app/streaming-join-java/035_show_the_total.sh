#!/bin/bash

set -e

docker exec -ti flinklab_taskmanager_1 sh -c "cat /tmp/total"
