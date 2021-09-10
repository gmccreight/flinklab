#!/bin/bash

set -e

function get_alert_count {

  set +e
  r=`docker exec -ti flinklab_taskmanager_1 sh -c "wc -l /tmp/ebj_alerts"`
  set -e

  v=`echo $r | cut -d' ' -f1`

  num_pat="^[0-9]+$"
  if [[ $v =~ $num_pat ]]; then
    echo $v
  else
    echo "0"
  fi

}

echo flinklab: verifying the streaming sensor ebj_alerts...

try_num=0
while true; do
  alert_count=`get_alert_count`
  echo "flinklab verify: try #$try_num - alert count was $alert_count"
  if (( $alert_count >= 1 )); then
    echo OK: there was at least one alert
    echo The latest alert was:
    docker exec -ti flinklab_taskmanager_1 sh -c "tail -n 1 /tmp/ebj_alerts"
    echo OK: we expected an alert on stream values exceeding a threshold, and we got at least one, so we are good
    break
  fi
  echo flinklab verify: sleeping for a second and trying again
  try_num=$(($try_num + 1))
  sleep 1
done
