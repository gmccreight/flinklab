### What is streaming-join-java

#### The Goal

You should be able to cd into this directory, run `000_all.sh`, and it should
run a multi-step set of KeyedProcessFunctions and ultimately output some totals.

The real goal is to have this in Scala, but I couldn't get it running *and* port it
to Scala in one go, so the Scala version will have to wait a bit.

### Assumptions

You have docker-compose installed and docker running.

### Details and Motivation

This code is mainly pulled from https://github.com/jamii/streaming-consistency/tree/main/flink-datastream
