#!/bin/bash

set -e

echo "Reading results written to the bucket"

docker exec -ti flinklab_minio_1 sh -c "cd /data/data/test-out; for i in \`ls | egrep \"[0-9]{4}-[0-9]{2}-[0-9]{2}--\"\`; do echo \$i; ls -al \$i; done"
