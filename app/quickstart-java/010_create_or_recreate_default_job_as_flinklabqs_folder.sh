#!/bin/bash

set -e

# Ensure you are in this directory
cd $(dirname "$0")

# Start over by removing the folder, then running the code generation again.
rm -rf flinklabqs

# The following code is super similar to https://flink.apache.org/q/quickstart-scala.sh
# ... but things have been renamed to flinklab and the quickstart
# project has been renamed flinklabqs.

PACKAGE=flinklabqs

$(git rev-parse --show-toplevel)/bin/mvn archetype:generate								\
  -DarchetypeGroupId=org.apache.flink				\
  -DarchetypeArtifactId=flink-quickstart-java		\
  -DarchetypeVersion=${1:-1.12.5}							\
  -DgroupId=org.flinklab.flinklabqs					\
  -DartifactId=$PACKAGE								\
  -Dversion=0.1										\
  -Dpackage=org.flinklab.flinklabqs					\
  -DinteractiveMode=false
