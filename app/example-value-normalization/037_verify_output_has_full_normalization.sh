#!/bin/bash

set -e

function get_normalized_and_change_published_count {

  set +e
  v=`docker exec -it flinklab_taskmanager_1 sh -c "grep thing_3 /tmp/evn_changes | grep true,true | wc -l" | sed -e 's/$//'`
  set -e

  num_pat="^[0-9]+$"
  if [[ $v =~ $num_pat ]]; then
    echo $v
  else
    echo "0"
  fi

}

echo flinklab: verifying the streaming normalization and published counts on thing_3 ...

try_num=0
while true; do
  change_count=`get_normalized_and_change_published_count`
  echo "flinklab verify: try #$try_num - output count was $change_count"
  if (( $change_count >= 1 )); then
    echo OK: there was at least one normalization that led to a change being published on thing_3
    echo "here are the latest 25 logged entries.  Most likely you'll see a true,true in there (and how the normalized value changes)"
    docker exec -it flinklab_taskmanager_1 sh -c "tail -n 25 /tmp/evn_changes | grep thing_3"
    echo OK: we expected some normalized and published changes and we saw them
    break
  fi
  echo flinklab verify: sleeping for a second and trying again
  try_num=$(($try_num + 1))
  sleep 1
done
