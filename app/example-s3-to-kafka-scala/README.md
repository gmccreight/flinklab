### What is example-s3-to-kafka?

#### The Goal

You should be able to cd into this directory, run `000_all.sh`, and it will
start by setting up a Kafka topics on a cluster running in Docker (starting the
cluster if necessary) and sync the `flinklab/data` directory to the MinIO
server (an S3-compatible file server). It will then package a jar and run the
job on a Flink cluster in Docker, which will monitor the `data/test/`
bucket/key. The script will then start a consumer that prints the contents of
the topic `example-out`, which should look like the `/data/test/test.csv` file.
Finally, it will cancel the still-running job.

#### Other usage
This project also provides a configurable way to get line-wise data from any
S3-like bucket into a Kafka stream.

With MinIO, the usage looks similar to what you see in the scripts. One thing
that may not be obvious is that the job _actively_ monitors the target key. So
for instance if you start the job as per `./030_run_jar_in_flink.sh`, then add
a new file
(e.g. `../../mc cp local/data/test/test.csv local/data/test/test2.csv`),
you will see the contents of this new file added
to the Kafka topic.

For remote S3 usage, we need to point our Flink config away from the default
config dir `flinklab/config.local` to `flinklab/config`, which we can do so
using an env file. Starting your cluster with the `.env.remote` config will do
just that:

```
$ docker-compose --env-file .env.remote up -d
```

You'll also need to make sure your `~/.aws/credentials` file
[contains valid credientials](https://docs.aws.amazon.com/cli/latest/userguide/cli-configure-files.html)
(e.g. by running `aws configure`).

At this point we can run the jar, pointing it at your desired bucket/key:

```
$ ../../bin/flink run -sae target/scala-2.11/s3-to-kafka-assembly-0.1.jar --sink-topic example-out --s3-bucket <MY_BUCKET> --s3-key <MY_KEY>
```

### Assumptions

You have docker-compose installed and docker running.

### Details

The `S3ToKafka` Flink job is conceptually quite simple, using the
[`StreamExecutionEnvironment`](https://ci.apache.org/projects/flink/flink-docs-release-1.12/dev/datastream_api.html#data-sources)
`readFile` method, which accepts a `watchType` parameter. In this case we have
chosen the `FileProcessingMode.PROCESS_CONTINUOUSLY` watch type, which monitors
the given directory and picks up any new files added. The `fileInputFormat`
parameter is also of interest, and in this case we have created a trivial
extension of the generic `GenericCsvInputFormat` class. Otherwise, all this
example does is connect the file source to a Kafka sink, similar to
`example-kafka-scala.`

Configuring everything correctly turned out to be the more difficult endeavour.
To start, we used the Hadoop backend for [S3
connectivity](https://ci.apache.org/projects/flink/flink-docs-master/docs/deployment/filesystems/s3/)
(apparently preferable for sources/sinks, whereas Presto is preferred for
checkpoints). As filesystems can only be loaded as
[_plugins_](https://ci.apache.org/projects/flink/flink-docs-master/docs/deployment/filesystems/plugins/),
the environment variable
`ENABLE_BUILT_IN_PLUGINS=flink-s3-fs-hadoop-1.12.5.jar` was added to
`docker-compose.yml`. Your local `~/.aws` directory is also mounted to
`/opt/flink/.aws` to allow your AWS credentials to be shared.

Configuring Flink to connect to either S3 or MinIO was a challenge, since these
both require different configuration parameters. In order to accommodate these
two modes, two different `flink-conf.yaml` files were created, one in
`flinklab/conf` and the other in `flinklab/conf.local`. The former contains

```
fs.s3a.aws.credentials.provider: com.amazonaws.auth.profile.ProfileCredentialsProvider
```

which tells the Hadoop connector to use `~./aws/credentials`, and the latter contains

```
s3.endpoint: http://minio:9000
s3.path.style.access: true
s3.access-key: minioadmin
s3.secret-key: minioadmin
```

which are the required settings to connect to the local MinIO server.

To allow the switching between these config files, two `.env` files were
created: `.env.remote` and `.env.local` (with `.env` being a copy of
`.env.local`). These files set the `FLINK_CONF_DIR` environment variable. Now,
when you start your Flink cluster, you can change how it connects to S3 by
either overwriting `.env` or by specifying a `.env` file, e.g.:

```
$ docker-compose --env-file .env.remote up -d taskmanager jobmanager
```
