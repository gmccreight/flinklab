### What is example-broadcast-join?

#### The Goal

You should be able to cd into this directory, run `000_all.sh`, and it will
compile and run a job that maintains the latest entries in a keyed stream
and normalizes them using a normalization mapping, outputting any changes.

### Assumptions

You have docker installed and running.

### Resources

The original code (which has been changed quite a bit) came from:

https://github.com/streaming-with-flink/examples-scala/blob/master/src/main/scala/io/github/streamingwithflink/chapter7/BroadcastStateFunction.scala
