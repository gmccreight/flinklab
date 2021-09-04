#!/bin/bash

set -e

# Ensure containers are running
docker compose up -d kafka

$(git rev-parse --show-toplevel)/bin/kafka topics --create --topic example-in --if-not-exists
$(git rev-parse --show-toplevel)/bin/kafka topics --create --topic example-out --if-not-exists
