## Usage

Use `./bin/flink` to interact with the project.

Commands:
- `./bin/flink package <application>`: Run `mvn package <application>/pom.xml`, compiling the program into `./target`
- `./bin/flink sh`: Access a bash console
- `./bin/flink *`: For anything else, this will act as the [`flink` CLI](https://ci.apache.org/projects/flink/flink-docs-master/docs/deployment/cli/) 

Preparing the docker containers:
```
docker-compose build
```

Starting the cluster:
```
docker-compose up -d
```

Compiling and running a program:

```
./bin/flink package app/quickstart-java/flinklabqs
./bin/flink run target/flinklabqs-0.1.jar
```

UI is hosted at http://localhost:8081


### Interacting with Kafka
```
# Create a new topic
$ ./bin/kafka topics --create --topic Topic1

# Describe it
$ ./bin/kafka topics --describe --topic Topic1

# List topics
$ ./bin/kafka topics --list

# Open a consumer for a topic
$ ./bin/kafka consumer --topic Topic1

# Open a producer for a topic (or use STDIN)
$ ./bin/kafka producer --topic Topic1
> [Add data to the topic here]

# Delete a topic
$ ./bin/kafka topics --delete --topic Topic1
```
