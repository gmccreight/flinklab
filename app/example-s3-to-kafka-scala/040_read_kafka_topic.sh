#!/bin/bash

set -e

echo "Reading from the topic. Never mind the 'Error processing message' error."
echo ""
echo "Topic data:"
# Get all messages from the beginning of the stream
$(git rev-parse --show-toplevel)/bin/kafka consumer \
                                --topic example-out \
                                --from-beginning \
                                --timeout-ms 1000
echo ""
