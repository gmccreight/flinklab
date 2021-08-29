#!/bin/bash

set -e

$(git rev-parse --show-toplevel)/bin/mvn clean
$(git rev-parse --show-toplevel)/bin/mvn package app/quickstart-scala/flinklabqs
