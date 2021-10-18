#!/bin/bash

set -e

$(git rev-parse --show-toplevel)/bin/kafka consumer --topic string-record-emitter-out --offset latest --partition 0 --max-messages 10
