### How to

Install all the prerequisite stuff described below (Java 8, Flink, Maven), then
cd into quickstart-scala, then run `./000_all.sh` and it will run a word count on
a cluster on your local machine and print the result to stdout.

Within that `quickstart-scala`, project tweak the code in the
`override_code_to_copy_into_flinklabqs`, and run the `./000_all.sh` script
again to see your changes.

### Details of Installation

We're ultimately going to be running on Amazon's Elastic Map Reduct (EMR),
which supports Flink 1.12, so that's what we'll be installing.  Specifically,
we'll be installing Flink 1.12.5.

The following is taken from the local installation instructions for 1.12 here...

https://ci.apache.org/projects/flink/flink-docs-release-1.12/try-flink/local_installation.html

... and tweaked a bit based on issues I encountered along the way.

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

We should download 1.12.5 from here:

https://www.apache.org/dyn/closer.lua/flink/flink-1.12.5/flink-1.12.5-bin-scala_2.11.tgz

Note how it has, or supports Scala 2.11.  I have no idea if that's important or not,
but I do know that Scala is the preferred language for flink development.

Also, note that the download has the example *.jar files, but not the source
code.  The source code for the example in question, WordCount, is here:
https://github.com/apache/flink/blob/release-1.12.5/flink-examples/flink-examples-streaming/src/main/java/org/apache/flink/streaming/examples/wordcount/WordCount.java

That's ok, though.  See the Readme inside our quickstart folder for how we deal with that.

Ok, once that's downloaded, you can

```
tar -xvzf flink-1.12.5-bin-scala_2.11.tgz
```

which will create a flink-1.12.5 folder.  I just cp'd that to my home folder.

```
cp -a flink-1.12.5 ~/
```

### Installing Maven

You can get Maven at https://maven.apache.org/download.cgi

The version I used is apache-maven-3.8.2-bin.tar.gz

After I unzipped it, I placed the folder in my home directory and updated my PATH and ran this...

`mvn -v`

... I got the following:

```
Apache Maven 3.8.2 (ea98e05a04480131370aa0c110b8c54cf726c06f)
Maven home: /Users/gordonmccreight/apache-maven-3.8.2
Java version: 1.8.0_301, vendor: Oracle Corporation, runtime: /Library/Java/JavaVirtualMachines/jdk1.8.0_301.jdk/Contents/Home/jre
Default locale: en_US, platform encoding: UTF-8
OS name: "mac os x", version: "10.15.7", arch: "x86_64", family: "mac"
```

### Creating a project

Check out out quickstart folder for how to actually create and run a project.

It's all scripted, so it *should* be easy, assuming you did the exact same installation I did above.
