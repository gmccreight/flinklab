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

# Add the target profile to pom.xml
docker-compose run --rm console /bin/bash -c 'cd quickstart-scala ; xmlstarlet ed -N x="http://maven.apache.org/POM/4.0.0" -s "/x:project" -t elem -n "profiles" -v "$(<override_code_to_copy_into_flinklabqs/target-profile.xml)" flinklabqs/pom.xml | xmlstarlet unesc | xmlstarlet fo > flinklabqs/pom_replacement.xml'

mv flinklabqs/pom_replacement.xml flinklabqs/pom.xml
