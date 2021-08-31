#!/bin/bash

set -e

# This will run the Scala test files named like: *ITCase.scala
# TODO: I have not been able to figure out how, so far... so I have just appened Test to the file
# so it is just run, incorrectly, as a unit test in step 025_run_unit_tests

# $(git rev-parse --show-toplevel)/bin/mvn integration-test app/example-wordcount-scala-with-unit-test/wc
