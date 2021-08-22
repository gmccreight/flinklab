#!/bin/bash

set -e

clear

this_abs_path="$( cd -- "$(dirname "$0")" >/dev/null 2>&1 ; pwd -P )"

flink_dir=~/flink-1.12.5

cd $flink_dir

# Stop the cluster to clean up state
if [[ `ps aux | grep java | grep flink` ]]; then
  echo stopping the cluster
  ./bin/stop-cluster.sh
fi

while [[ `ps aux | grep java | grep flink` ]]; do
  echo "`date`: waiting for cluster to stop"
  sleep 1
done

# Start the cluster if it's not already running
if ! [[ `ps aux | grep java | grep flink` ]]; then
  echo starting the cluster
  ./bin/start-cluster.sh
fi

rm -f log/flink-*-taskexecutor-*.out

./bin/flink run $this_abs_path/flinklabqs/target/flinklabqs-0.1.jar

# Now you can look at http://localhost:8081/

for i in `seq 1 10`; do
  echo "`date`: $i of 10: showing one line of log output, then pausing for a second at `date`"
  echo ""
  if [[ `ls log | grep "taskexecutor-.*\.out"` ]]; then
    tail -n 1 log/*taskexecutor-*.out
  fi
  sleep 1
done
exit

