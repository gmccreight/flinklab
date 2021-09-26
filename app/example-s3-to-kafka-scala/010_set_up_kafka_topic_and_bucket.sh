#!/bin/bash

set -e

# Ensure containers are running
docker compose up -d kafka minio

echo "Creating topic"
$(git rev-parse --show-toplevel)/bin/kafka topics --delete --topic example-out --if-exists
$(git rev-parse --show-toplevel)/bin/kafka topics --create --topic example-out


echo "Syncing data bucket"
# This data is on the local filesystem at .minio-data/data, which is mounted as
# /data in the minio container.
#
# The command `mc sync` is special in the same way that `mc sh` is.
# It does not run an mc command, but rather it runs this:
# `docker-compose run --rm console mc mirror --overwrite --remove /data local/data`
# which takes the data that is in the /data directory and actually loads it into the 'data' bucket
$(git rev-parse --show-toplevel)/bin/mc sync
