#!/bin/bash

# The earlier step installed the default code into the flinklabqs folder, which
# contains some pretty empty boilerplate.  But we have some WordCount code we
# actually want to run, instead, in the override_code_to_copy_into_flinklabqs
# folder.  So copy that into place and update the pom.xml

set -e

# Ensure you are in this directory
cd $(dirname "$0")

rm -f flinklabqs/src/main/java/org/flinklab/flinklabqs/*

# Copy the code from the folder to the destination
cp override_code_to_copy_into_flinklabqs/*.java flinklabqs/src/main/java/org/flinklab/flinklabqs

# Replace the StreamingJob with WordCount
cat flinklabqs/pom.xml | sed -e 's/StreamingJob/WordCount/' > flinklabqs/pom_replacement.xml
mv flinklabqs/pom_replacement.xml flinklabqs/pom.xml
