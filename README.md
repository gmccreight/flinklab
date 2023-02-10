## How to

This repository is designed so that each directory in `app` is a Flink program. You can `cd` into one and run `./000_all.sh` to set up the program from scratch and run it. All you need as a prerequisite is `docker-compose` and an active docker installation (e.g. [Docker Desktop](https://www.docker.com/products/docker-desktop)).

For instance, `cd app/quickstart-scala ; ./000_all.sh` will run a word count on
a cluster on your local machine and print the result to stdout. Within that `quickstart-scala` project, tweak the code in the `override_code_to_copy_into_flinklabqs`, and run the `./000_all.sh` script again to see your changes.

## Usage

Use the following commands to interact with a program. These commands must be run from within this project, and they respect the current directory.

Commands:
- `./bin/mvn *`: Run any `mvn` command
- `./bin/sbt *`: Run any `stb` command
- `./bin/flink *`: Run any [`flink` command](https://ci.apache.org/projects/flink/flink-docs-master/docs/deployment/cli/) 
- `./bin/flink sh`: Access a bash console
- `./bin/flink scala [remote]`: Access a Flink Scala console (local or remote, which requires `jobmanager` to be started)

Preparing the docker containers:
```
$ docker-compose build
```

Starting the cluster:
```
$ docker-compose up -d
```

Compiling and running a program:

```
$ cd app/quickstart-scala/flinklabqs
$ ./bin/mvn package
$ ./bin/flink run target/flinklabqs-0.1.jar
```

Flink UI is hosted at http://localhost:8081 when `jobmanager` is running.

### Clearing the cache
There are three cache directories, which get mounted to the `console` service, that are used to house the Maven and sbt caches: `.m2`, `.sbt`, and `.cache`. You can use the `./bin/mvn clear-cache` and `./bin/sbt clear-cache` commands to clear these caches.

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

### Interacting with MinIO
[MinIO](https://min.io/) is a S3-compatible file server that is included in this docker compose environment.

MinIO provides a command-line app, `mc`, through which you can interact with not just MinIO, but local files and any other S3-compatible file servers (like S3, and GCS). You can use the `./bin/mc` command, but installing it locally (e.g. `brew install minio/stable/minio`) may be more convenient. When installing locally, be sure configure it correctly (e.g. `cp flinklab/.mc/config.json ~/.mc/config.json`).

When running, you can go to its web UI http://localhost:9001/ to view files and interact with the server.

The local MinIO files are stored in `.minio-data` and it has been initialized with one bucket: `data`. This bucket was created to line up with the `flinklab/data` directory, and files from the latter can be synced to the bucket using the `./bin/mc sync` command (not a standard mc command).

## Creating a project

Check out one of the quickstart folders for how to actually create and run a project.

It's all scripted, so it *should* be easy.
