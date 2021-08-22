#!/bin/bash

# The earlier step installed the default code into the flinklabqs folder, which
# contains some pretty empty boilerplate.  But we have some WordCount code we
# actually want to run, instead, in the override_code_to_copy_into_flinklabqs
# folder.  So copy that into place and update the pom.xml

set -e

rm -f flinklabqs/src/main/scala/org/flinklab/flinklabqs/*

# Copy the code from the folder to the destination
cp override_code_to_copy_into_flinklabqs/WordCount.scala flinklabqs/src/main/scala/org/flinklab/flinklabqs

# Replace the StreamingJob with WordCount
cat flinklabqs/pom.xml | sed -e 's/StreamingJob/WordCount/' > flinklabqs/pom_replacement.xml
mv flinklabqs/pom_replacement.xml flinklabqs/pom.xml
