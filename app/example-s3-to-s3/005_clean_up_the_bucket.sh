#!/bin/bash

echo "cleaning up the older output files in the bucket"

docker exec -ti flinklab_minio_1 sh -c "rm -rf /data/data/test-out"
