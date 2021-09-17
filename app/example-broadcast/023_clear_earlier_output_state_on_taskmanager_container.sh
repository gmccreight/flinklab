#!/bin/bash

set -e

# We need to clear this to ensure that our 035_verify_the_output.sh script,
# isn't just verifying output from earlier runs, accidentally

for tm_num in 1 2; do
  docker exec -ti flinklab_taskmanager_$tm_num sh -c "rm -f /tmp/eb_output"
done
