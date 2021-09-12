#!/bin/bash

set -e

# Ensure containers are running
docker compose up -d kafka minio

echo "Creating topic"
$(git rev-parse --show-toplevel)/bin/kafka topics --delete --topic example-out --if-exists
$(git rev-parse --show-toplevel)/bin/kafka topics --create --topic example-out

echo "Syncing data bucket"
$(git rev-parse --show-toplevel)/bin/mc sync
