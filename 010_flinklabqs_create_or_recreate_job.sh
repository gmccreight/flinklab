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

#
# Give some guidance
#
echo -e "\\n\\n"
echo -e "\\tA sample quickstart Flink Job has been created."
echo -e "\\tSwitch into the directory using"
echo -e "\\t\\t cd $PACKAGE"
echo -e "\\tImport the project there using your favorite IDE (Import it as a maven project)"
echo -e "\\tBuild a jar inside the directory using"
echo -e "\\t\\t mvn clean package"
echo -e "\\tYou will find the runnable jar in $PACKAGE/target"
echo -e "\\tConsult our website if you have any troubles: http://flink.apache.org/community.html#mailing-lists"
echo -e "\\n\\n"
