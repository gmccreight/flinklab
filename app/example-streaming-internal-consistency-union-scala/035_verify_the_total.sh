#!/bin/bash

set -e

# If you want to see the credits and debits data, you can uncomment these

# echo
# echo "Example of one line of the /tmp/credits intermediate data:"
# docker exec -ti flinklab_taskmanager_1 sh -c "cat /tmp/credits" | tail -n 1
# 
# echo
# echo "Example of one line of the /tmp/debits intermediate data:"
# docker exec -ti flinklab_taskmanager_1 sh -c "cat /tmp/debits" | tail -n 1

echo
echo "Example of one line of the /tmp/total output:"
docker exec -ti flinklab_taskmanager_1 sh -c "cat /tmp/total" | tail -n 1

echo
echo "How many lines have the expected value of 0.0?"
docker exec -ti flinklab_taskmanager_1 sh -c "cat /tmp/total" | grep ',0.0)' | wc -l

echo
echo "How many lines have an unexpected value?"
docker exec -ti flinklab_taskmanager_1 sh -c "cat /tmp/total" | grep -v ',0.0)' | wc -l
echo
