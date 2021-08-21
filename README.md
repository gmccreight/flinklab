### Installation

We're going to be running on Amazon's Elastic Map Reduct (EMR), which supports
Flink 1.12, so that's what we'll be installing.  Specifically, we'll be installing
Flink 1.12.5.

The following is taken from the local installation instructions for 1.12 here:
https://ci.apache.org/projects/flink/flink-docs-release-1.12/try-flink/local_installation.html

and tweaked a bit based on issues I encountered along the way.

#### Installing Java

Flink 1.12 cannot run on Java 16, which is the Java that the mac will try to
install if you don't have Java and run `java -version` (it will throw up a
popup).

Instead of doing that, get Java 8 or 11 instead.  I opted for 8. You can get it here:
https://www.oracle.com/java/technologies/javase/javase-jdk8-downloads.html

Unlike Java 16, to get Java 8 you'll need to sign up for an Oracle
account, which is super obnoxious.  I know.  In 2021.  Can you believe it?

The version I downloaded was jdk-8u301-macosx-x64.dmg

Once installed, you should get the following (or something similar, but there
should definitely not be 16 mentioned anywhere in there) when you run
`java -version`

```
java version "1.8.0_301"
Java(TM) SE Runtime Environment (build 1.8.0_301-b09)
Java HotSpot(TM) 64-Bit Server VM (build 25.301-b09, mixed mode)
```

#### Installing Flink

Ok, so as mentioned above, we need version 1.12, since it's supported by EMR.

To be able to see that things are working well, we'll want to be able to run
the examples, too.  This means that we should *not* use the homebrew version, since it
does not come with the examples.  Rather, we should download 1.12.5 from here:

https://www.apache.org/dyn/closer.lua/flink/flink-1.12.5/flink-1.12.5-bin-scala_2.11.tgz

Note how it has, or supports Scala 2.11.  I have no idea if that's important or not,
but I do know that Scala is the preferred language for flink development.

Also, note that the download has the example *.java* files, but not the source
code.  The source code for the example in question, WordCount, is here:
https://github.com/apache/flink/blob/release-1.12.5/flink-examples/flink-examples-streaming/src/main/java/org/apache/flink/streaming/examples/wordcount/WordCount.java

Ok, once that's downloaded, you can

```
tar -xvzf flink-1.12.5-bin-scala_2.11.tgz
```

which will create a flink-1.12.5 folder.  I just cp'd that to my home folder.

```
cp -a flink-1.12.5 ~/
```

From there, the code in the `dev.sh` file should work.
