### Development Notes

#### Problem: directory name too long

Kinda cryptic error:

```
sbt thinks that server is already booting because of this exception:
java.io.IOException: org.scalasbt.ipcsocket.NativeErrorException: [22] Invalid argument
```

But another directory worked.  Winnowed the issue down to the length of the directory name.

#### Solution: 

Figured out how long would work

```
example-wordcount-scala-sbt-0123456789  <- works
example-wordcount-scala-sbt-01234567890 <- too long

1234567890123456789012345678901234567890 (ruler)
         10        20        30
```

Shortened the directory name to ensure it worked.

#### Problem: jcuda jar

```
[error] lmcoursier.internal.shaded.coursier.error.FetchError$DownloadingArtifacts: Error fetching artifacts:
[error] file:/root/.m2/repository/org/jcuda/jcuda-natives/10.0.0/jcuda-natives-10.0.0-linux-x86_64.jar: not found: /root/.m2/repository/org/jcuda/jcuda-natives/10.0.0/jcuda-natives-10.0.0-linux-x86_64.jar
```

#### Non-solutions:

Tried adding:

```
"org.jcuda" % "jcuda-natives" % "linux-x86_64" % "10.0.0"
```

I also tried:

```
javaOptions ++= Seq(
  "-Djcuda.os=linux",
  "-Djcuda.arch=x86_64"
)
```


#### Solution:

There was an existing directory in our m2 cache, which we map as a volume into /root/.m2 in docker.

I removed that directory, and that caused the jcuda stuff to be re-downloaded
the next time it ran, then that worked ok.

rm -r .m2/repository/org/jcuda


#### Problem: scalatest is different from junit

#### Not a Solution

Use cloudflow.flink.testkit._

https://developer.lightbend.com/docs/cloudflow/current/develop/test-flink-streamlet.html

import cloudflow.flink.testkit._

then in ScalaTest, you...

```
class FlinkProcessorSpec Extend from the abstract class FlinkTestkit
```

#### Solution

Found https://github.com/houcros/linearroad/blob/e03507182310b99387e9c7c7078989c22ff70439/src/test/scala/de/tu_berlin/dima/bdapro/flink/linearroad/houcros/flink/LatestAverageVelocitySpec.scala

by searching Github for "BeforeAndAfterAll StreamExecutionEnvironment".  BeforeAndAfterAll is part of scalatest, and StreamExecutionEnvironment is part of the test in example-wordcount-scala-with-integration-and-unit-tests

I used that approach.


#### Problem: LoggerFactory class not found

Starts running the unit tests

```
[info] WordCountSpec:
[info] The stream processing
[info] Exception encountered when attempting to run a suite with class name: WordCountSpec *** ABORTED ***
[info]   java.lang.NoClassDefFoundError: org/slf4j/LoggerFactory
[info]   at org.apache.flink.configuration.Configuration.<clinit>(Configuration.java:63)
[info]   at org.apache.flink.streaming.api.environment.StreamExecutionEnvironment.getExecutionEnvironment(StreamExecutionEnvironment.java:203
```

#### Solution: 

removing all the .m2/repository directories and trying again

#### Problem: matching two arrays

In the unit tests, I wanted to compare two arrays

#### Solution

https://www.scalatest.org/scaladoc/1.7.2/org/scalatest/matchers/ShouldMatchers.html
