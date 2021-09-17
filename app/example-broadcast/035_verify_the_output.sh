#!/bin/bash

set -e

function get_output_count {

  tm_num=$1

  set +e
  r=`docker exec -ti flinklab_taskmanager_$tm_num sh -c "wc -l /tmp/eb_output"`
  set -e

  v=`echo $r | cut -d' ' -f1`

  num_pat="^[0-9]+$"
  if [[ $v =~ $num_pat ]]; then
    echo $v
  else
    echo "0"
  fi

}

function verify_output_checking_multiple_taskmanagers {
  try_num=0
  while true; do
    for tm_num in 1 2; do
      echo trying on taskmanager number $tm_num
      output_count=`get_output_count $tm_num`
      echo "flinklab verify: try #$try_num - output count was $output_count"
      if (( $output_count >= 1 )); then
        echo OK: there was at least one output
        echo The latest output was:
        docker exec -ti flinklab_taskmanager_$tm_num sh -c "tail -n 30 /tmp/eb_output"
        exit
      fi
      echo flinklab verify: sleeping for a second and trying again
      try_num=$(($try_num + 1))
      sleep 1
    done
  done
}

echo flinklab: verifying the example_broadcast eb_output...
verify_output_checking_multiple_taskmanagers
