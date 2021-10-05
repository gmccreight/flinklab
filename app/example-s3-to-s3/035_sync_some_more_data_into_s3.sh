#!/bin/bash

set -e

echo "Syncing more data into S3"

sleep 5

for i in `seq 1 1000`; do
  echo "$i,$i,$i" >> ../../.minio-data/data/test/more.csv
done

# This data is on the local filesystem at .minio-data/data, which is mounted as
# /data in the minio container.
#
# The command `mc sync` is special in the same way that `mc sh` is.
# It does not run an mc command, but rather it runs this:
# `docker-compose run --rm console mc mirror --overwrite --remove /data local/data`
# which takes the data that is in the /data directory and actually loads it into the 'data' bucket

$(git rev-parse --show-toplevel)/bin/mc sync

sleep 10
