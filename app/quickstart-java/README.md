### What is quickstart?

#### The Goal

You should be able to cd into this directory, run `000_all.sh`, and it should
compile a WordCount jar.  Then it should start a Flink cluster on your local
machine, run the WordCountData job, and report some results.

### Assumptions

You have docker-compose installed and docker running.

### Details and Motivation

You should probably stop reading here; we're about to get into tedious details.

Still interested?  Ok, so there are a _lot_ of tedious details needed to
actually get a useful Flink job running from source.

Flink provides a [quickstart script](https://flink.apache.org/q/quickstart.sh),
but it creates a generic set of empty files that don't atually run anything.

They also provide a nice WordCount example that *does* do something interesting, but it only
comes as a jar in the Flink download (and doesn't come at all if you use Homebrew, which you should not have done).
Jar files, and even class files aren't so helpful if you want to experiment!  You need .java files!

Anyhow, there *is* a .java source version of it
[here](https://github.com/apache/flink/tree/release-1.12.5/flink-examples/flink-examples-streaming/src/main/java/org/apache/flink/streaming/examples/wordcount),
but it's tangled in with a bunch of other examples, the paths are wrong,
etc.

So anyhow, the folder structure created by the Flink quickstart script is
pretty important to get (and keep) right.  So rather than simply using the
quickstart script they provide one time, committing it to git, then making
hard-to-reason-about modifications to the code in that folder, after-the-fact,
I'm layering in changes as part of a small build process.

You can see that in the `015_make_changes_to_default_job.sh` script.

Having the steps be clearly labeled with ordering numbers (and having `000_all.sh`
as an easy place to orchestrate the steps) is also very helpful for reasoning
about things.
