#!/bin/bash

set -e

echo "for thing_3, you should see a true at the end if when is a change"
docker exec -ti flinklab_taskmanager_1 sh -c "grep thing_3 /tmp/evn_changes"
