### What is example-streaming-internal-consistency-union-java

#### The Goal

You should be able to cd into this directory, run `000_all.sh`, and prove to
yourself that we can handle processing two separate streams in an internally
consistent way.

### Assumptions

You have docker-compose installed and docker running.

### Details and Motivation

From [the blog post about internal consistency in streaming systems](https://scattered-thoughts.net/writing/internal-consistency-in-streaming-systems/):

> Internal consistency allows you to pretend that you're just operating a very fast batch system instead of having to reason about all the possible interleavings of updates through your streaming topology.

That sounds like an important goal!

The blog post questioned whether that was possible in Flink.  This code was Vasia Kalavri's response, which showed that it is, but not with the higher-level APIs.  Rather, she used KeyedProcessFunction, which gives you (and requires) more manual control.

This example's code is mainly pulled from https://github.com/jamii/streaming-consistency/tree/main/flink-datastream
