#!/bin/bash

set -e

clear

flink_dir=~/flink-1.12.5

cd $flink_dir

# The examples don't come with source code, but you can see the code here:
# https://github.com/apache/flink/blob/release-1.12.5/flink-examples/flink-examples-streaming/src/main/java/org/apache/flink/streaming/examples/wordcount/WordCount.java

./bin/start-cluster.sh
# Now you can look at http://localhost:8081/

# ./bin/flink run examples/streaming/WordCount.jar
# tail log/flink-*-taskexecutor-*.out

# ./bin/stop-cluster.sh
