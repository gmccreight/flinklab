#!/bin/bash

set -e

function get_output_count {

  set +e
  r=`docker exec -ti flinklab_taskmanager_1 sh -c "wc -l /tmp/eb_output"`
  set -e

  v=`echo $r | cut -d' ' -f1`

  num_pat="^[0-9]+$"
  if [[ $v =~ $num_pat ]]; then
    echo $v
  else
    echo "0"
  fi

}

echo flinklab: verifying the streaming sensor eb_output...

try_num=0
while true; do
  output_count=`get_output_count`
  echo "flinklab verify: try #$try_num - output count was $output_count"
  if (( $output_count >= 1 )); then
    echo OK: there was at least one output
    echo The latest output was:
    docker exec -ti flinklab_taskmanager_1 sh -c "tail -n 10 /tmp/eb_output"
    break
  fi
  echo flinklab verify: sleeping for a second and trying again
  try_num=$(($try_num + 1))
  sleep 1
done
