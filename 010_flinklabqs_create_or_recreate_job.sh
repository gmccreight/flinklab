#!/bin/bash

set -e

# Start over by removing the project, then running the code again.
rm -rf flinklabqs

# The following comes from https://flink.apache.org/q/quickstart.sh
# ... but things have been renamed to flinklab and the quickstart
# project has been renamed flinklabqs.

PACKAGE=flinklabqs

mvn archetype:generate								\
  -DarchetypeGroupId=org.apache.flink				\
  -DarchetypeArtifactId=flink-quickstart-java		\
  -DarchetypeVersion=${1:-1.12.5}							\
  -DgroupId=org.flinklab.flinklabqs					\
  -DartifactId=$PACKAGE								\
  -Dversion=0.1										\
  -Dpackage=org.flinklab.flinklabqs					\
  -DinteractiveMode=false

# Copy the code from the folder to the destination
rm flinklabqs/src/main/java/org/flinklab/flinklabqs/BatchJob.java
rm flinklabqs/src/main/java/org/flinklab/flinklabqs/StreamingJob.java
cp flinklabqs_code/*.java flinklabqs/src/main/java/org/flinklab/flinklabqs

# Replace the StreamingJob with WordCount
cat flinklabqs/pom.xml | sed -e 's/StreamingJob/WordCount/' > flinklabqs/pom_replacement.xml
mv flinklabqs/pom_replacement.xml flinklabqs/pom.xml
