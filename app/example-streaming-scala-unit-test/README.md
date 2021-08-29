### What is example-test-scala?

#### The Goal

An extremely simple single unit test, written in Scala, that shows how to test
Flink's streaming API.

You should be able to cd into this directory, run `000_all.sh`, and it will
run that single streaming test successfully.

### Non-goals

We are not trying to test an actual project in this example, hence there is
no code in the `src` directory.  Also, the pom.xml has been reduced
as much as possible while still supporting our goal.

A future goal will be to integrate tests into an actual project, but that will be
a separate example.  We want to keep this example as simple as possible.

### Assumptions

You have docker-compose installed and docker running.

### References

Unlike the quickstart-java and quickstart-scala projects, this project was not
created by running the quickstart scripts and making slight modifications.
Rather, I pulled significant pieces of it from
https://github.com/tracholar/ml-homework-cz/blob/7e1628b7b4afa1f4290d51a9e30ffbc84cab8ff0/flink/tracholar/src/test/scala/com/tracholar/flink/demo/DemoTest.scala
, then reduced the pom.xml as much as possible, and added our expected Flink
and Scala versions to it.
