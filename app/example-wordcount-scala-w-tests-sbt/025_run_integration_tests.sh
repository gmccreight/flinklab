#!/bin/bash

# Integration tests are in...
# src/it
# in the build.sbt
# you see something like...
# "org.scalatest" %% "scalatest" % "2.2.4" % "it,test"
# The "it" there stands for IntegrationTest

set -e

# Move to this dir
cd $(dirname "$0")

echo "Running Integration Tests"
$(git rev-parse --show-toplevel)/bin/sbt IntegrationTest/test
