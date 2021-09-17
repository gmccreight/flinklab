### What is example-broadcast-join?

#### The Goal

You should be able to cd into this directory, run `000_all.sh`, and it will
compile and run a job that joins a broadcast stream with another stream.

It also verifies that the processing is working correctly.

### Assumptions

You have docker installed and running.

### How to read the output

The output looks like this (I added the line numbers):

```
1 : BEFORE: null AFTER: Value=(1,5) Broadcast State=[]
2 : Broadcast taskIndex:0, key:1, state.value:Value=(1,5) Broadcast State=[]
3 : BEFORE: null AFTER: Value=(2,6) Broadcast State=[70->700 ]
4 : Broadcast taskIndex:0, key:1, state.value:Value=(1,5) Broadcast State=[]
5 : Broadcast taskIndex:0, key:2, state.value:Value=(2,6) Broadcast State=[70->700 ]
6 : BEFORE: null AFTER: Value=(3,7) Broadcast State=[90->900 70->700 ]
7 : BEFORE: null AFTER: Value=(4,8) Broadcast State=[90->900 70->700 ]
8 : BEFORE: Value=(1,5) Broadcast State=[] AFTER: Value=(1,1) Broadcast State=[90->900 70->700 ]
9 : BEFORE: Value=(2,6) Broadcast State=[70->700 ] AFTER: Value=(2,2) Broadcast State=[90->900 70->700 ]
10: BEFORE: Value=(3,7) Broadcast State=[90->900 70->700 ] AFTER: Value=(3,3) Broadcast State=[90->900 70->700 ]
11: BEFORE: Value=(4,8) Broadcast State=[90->900 70->700 ] AFTER: Value=(4,4) Broadcast State=[90->900 70->700 ]
12: Broadcast taskIndex:0, key:4, state.value:Value=(4,4) Broadcast State=[90->900 70->700 ]
13: Broadcast taskIndex:0, key:1, state.value:Value=(1,1) Broadcast State=[90->900 70->700 ]
14: Broadcast taskIndex:0, key:3, state.value:Value=(3,3) Broadcast State=[90->900 70->700 ]
15: Broadcast taskIndex:0, key:2, state.value:Value=(2,2) Broadcast State=[90->900 70->700 ]
```

Before we dig into this, I will point out that I made the Broadcast state
numbers bigger than the elementStream's numbers, and their value part 10x
bigger, so there wouldn't be any confusion about how they relate to the values
in processElement.

Ok, let's take that section by section...

First, the processElement function is called.  There is no before value.  The
after value is the tuple of the element being processed.  There is no Broadcast
State yet.

```
1 : BEFORE: null AFTER: Value=(1,5) Broadcast State=[]
```

Next, the processBroadcastElement function is called.  When it runs the
applyToKeyedState function, there is only one keyedState stored so far, which
is why you only see one line.

The reason why the Broadcast State is empty is because it's just printing the
keyed state again, and that whole "Value=(1,5) Broadcast State=[]" is just a
string stored in the key state.  It's a little confusing, since the broadcast
state *just* updated, so it is surprising to not see it listed there, but it
makes sense, because the keyed state, where that string comes from, was not
updated.

```
2 : Broadcast taskIndex:0, key:1, state.value:Value=(1,5) Broadcast State=[]
```

The processElement function is called again.  There is no before value.  This
time the value (2,6) is stored, but also the Broadcast State, which was stored
in the previous step, is now saved into the stored string.

```
3 : BEFORE: null AFTER: Value=(2,6) Broadcast State=[70->700 ]
```

Next, the processBroadcastElement function is called again.  This time, there are
two values stored in the state.  It iterates over both of them.  The reason why there
is no Broadcast State in line 4 is the same reason there wasn't one before.  The string
"Value=(1,5) Broadcast State=[]" is still what's stored in the ValueState for key 1


```
4 : Broadcast taskIndex:0, key:1, state.value:Value=(1,5) Broadcast State=[]
5 : Broadcast taskIndex:0, key:2, state.value:Value=(2,6) Broadcast State=[70->700 ]
```

Now we just happen to have two processElement function calls before another processBroadcastElement call

```
6 : BEFORE: null AFTER: Value=(3,7) Broadcast State=[90->900 70->700 ]
7 : BEFORE: null AFTER: Value=(4,8) Broadcast State=[90->900 70->700 ]
```

At this point, we still expect 4 more processElement calls and one more processBroadcastElement call.

The 4 more processElement calls happen to occur now.  Notice how the elements
are processed a little out-of-order?  Before is (1,5), but after is (1,1)?
Sometimes when you run this, it goes the other way.

```
8 : BEFORE: Value=(1,5) Broadcast State=[] AFTER: Value=(1,1) Broadcast State=[90->900 70->700 ]
9 : BEFORE: Value=(2,6) Broadcast State=[70->700 ] AFTER: Value=(2,2) Broadcast State=[90->900 70->700 ]
10: BEFORE: Value=(3,7) Broadcast State=[90->900 70->700 ] AFTER: Value=(3,3) Broadcast State=[90->900 70->700 ]
11: BEFORE: Value=(4,8) Broadcast State=[90->900 70->700 ] AFTER: Value=(4,4) Broadcast State=[90->900 70->700 ]
```

Ok, now we expect the third and final processBroadcastElement.  We also expect
its applyToKeyedState to apply to all the pieces of keyed state (which is 4 at
this point), and it does:

```
12: Broadcast taskIndex:0, key:4, state.value:Value=(4,4) Broadcast State=[90->900 70->700 ]
13: Broadcast taskIndex:0, key:1, state.value:Value=(1,1) Broadcast State=[90->900 70->700 ]
14: Broadcast taskIndex:0, key:3, state.value:Value=(3,3) Broadcast State=[90->900 70->700 ]
15: Broadcast taskIndex:0, key:2, state.value:Value=(2,2) Broadcast State=[90->900 70->700 ]
```



### Resources

The code is originally from:

https://github.com/a0x8o/flink/blob/master/flink-examples/flink-examples-streaming/src/main/scala/org/apache/flink/streaming/scala/examples/broadcast/BroadcastExample.scala

I have made some moderate changes to it, in the name of trying to make it make more sense.
