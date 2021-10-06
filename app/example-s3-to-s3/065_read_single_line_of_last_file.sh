#!/bin/bash

set -e

echo "Reading a single line of the last file .csv file in the most recent directory"

docker exec -ti flinklab_minio_1 sh -c "cd /data/data/test-out; i=\`ls | egrep \"[0-9]{4}-[0-9]{2}-[0-9]{2}--\" | tail -n 1\` ; cd \$i; h=\`ls | egrep \"^part.*csv\" | head -n 1\`; echo \$h; tail -n 1 \$h"
