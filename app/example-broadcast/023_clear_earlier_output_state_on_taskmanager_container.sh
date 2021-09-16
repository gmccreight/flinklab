#!/bin/bash

set -e

# We need to clear this to ensure that our 035_verify_the_output.sh script,
# isn't just verifying output from earlier runs, accidentally

docker exec -ti flinklab_taskmanager_1 sh -c "rm -f /tmp/eb_output"
