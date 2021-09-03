### What is example-kafaka?

#### The Goal

You should be able to cd into this directory, run `000_all.sh`, and it will start by setting up two Kafka topics on a cluster running in Docker (starting the cluster if necessary). It will then package a KafkaExample jar and run the job on a Flink cluster in Docker, which will consume from a Kafka topic `example-in`. The script will then start a consumer that monitors and echos the topic `example-out`, and it will send a series of words into `example-in`. You should see the words echoed back by the monitor with the string `[processed]` appended. Finally, it will cancel the still-running job.

### Assumptions

You have docker-compose installed and docker running.
