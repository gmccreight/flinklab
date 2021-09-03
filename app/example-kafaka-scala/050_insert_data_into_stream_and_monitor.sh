#!/bin/bash

set -e

# Run the consumer
$(git rev-parse --show-toplevel)/bin/kafka consumer --topic example-out &

# Wait for the consumer to start up
sleep 1

# Insert data into the input stream
docker-compose exec kafka /bin/bash -c '/opt/kafka/bin/kafka-console-producer.sh --bootstrap-server=kafka:9092 --topic example-in <<EOF
the
quick
brown
fox
jumped
over
the
lazy
dog
EOF
'

# This could also be done by:
#
# $ ./bin/kafka producer --topic example-in
#
# And adding your own data interactively

# Wait for the consumer to receive the data
sleep 1

# Because I've backgrounded the consumer, I'm not totally sure how to have it exit cleanly when the script ends, but it doesn't really matter
echo "
Done. You can ignore this 'Unable to close the console' error message:"
