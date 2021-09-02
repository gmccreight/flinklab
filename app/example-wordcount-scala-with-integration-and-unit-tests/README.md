### What is quickstart?

#### The Goal

You should be able to cd into this directory, run `000_all.sh`, and it should
package a WordCount jar.  As part of that packaging, it will run some
integration and unit tests.  Finally, if those tests pass, it should start a
Flink cluster on your local machine, run the WordCountData job, and report some
results.

### Assumptions

You have docker-compose installed and docker running.

### Details and Motivation

Unlike the quickstart-scala project
