#!/bin/bash

set -e

function get_change_count {

  set +e
  r=`docker exec -ti flinklab_taskmanager_1 sh -c "wc -l /tmp/evn_changes"`
  set -e

  v=`echo $r | cut -d' ' -f1`

  num_pat="^[0-9]+$"
  if [[ $v =~ $num_pat ]]; then
    echo $v
  else
    echo "0"
  fi

}

echo flinklab: verifying the streaming normalization evn_changes...

try_num=0
while true; do
  change_count=`get_change_count`
  echo "flinklab verify: try #$try_num - output count was $change_count"
  if (( $change_count >= 1 )); then
    echo OK: there was at least one change
    echo The latest change was:
    docker exec -ti flinklab_taskmanager_1 sh -c "tail -n 20 /tmp/evn_changes"
    echo OK: we expected some changes and we saw them
    break
  fi
  echo flinklab verify: sleeping for a second and trying again
  try_num=$(($try_num + 1))
  sleep 1
done
