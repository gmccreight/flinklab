### What is example-value-normalization

#### The Goal

You should be able to cd into this directory, run `000_all.sh`, and it will
compile and run a job that maintains the latest ThingUpdate records in a keyed
stream and normalizes them using a normalization mapping that is broadcast to
all, outputting any changes.

### Assumptions

You have docker installed and running.


### Details

The output stream looks like this:

```
              _ the normalized value
             /
             |  _ metadata_HasNormalizationValue
             | /
             | |     _ metadata_isChange
             | |    /
             | |    |
1 (thing_3,3,3,false,false)
2 (thing_3,3,1,true,true)
3 (thing_3,3,2,true,true)
4 (thing_3,3,2,true,false)
5 (thing_3,3,1,true,true)
```

In line 1, no normalization configuration has happened yet, so the normalized
value is the same as the input value.

In line 2, there is a normalization configuration, which cause the output normalized
value to change, causing the record to be published (AKA true, true).

Then in line 3, the normalized value changes, so another record is output.

In line 4, however, the normalization configuration did *not* change, so the
`metadata_isChange` is false.  In a production system, we would likey not publish
that record, since it is redundant, unless we cared about tracking the last time
of consideration.

Finally in line 5 there *is* a change again, and we would publish that.

In `037_verify_output_has_full_normalization.sh` we look for a true,true output
to ensure that the ThingUpdate was normalized and published at least once.

### Resources

The original code (which has been changed quite a bit) came from:

https://github.com/streaming-with-flink/examples-scala/blob/master/src/main/scala/io/github/streamingwithflink/chapter7/BroadcastStateFunction.scala
