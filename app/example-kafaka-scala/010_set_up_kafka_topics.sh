#!/bin/bash

set -e

# Ensure containers are running
docker compose up -d

$(git rev-parse --show-toplevel)/bin/kafka topics --create --topic example-in
$(git rev-parse --show-toplevel)/bin/kafka topics --create --topic example-out
