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

sleep 1

# Wait until it's *really* running before creating the job

while ! [[ `ps aux | grep java | grep flink` ]]; do
  echo "`date`: waiting for cluster to start"
  sleep 1
done

./bin/flink run $this_abs_path/flinklabqs/target/flinklabqs-0.1.jar
