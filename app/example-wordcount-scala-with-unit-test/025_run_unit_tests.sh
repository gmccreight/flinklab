#!/bin/bash

set -e

$(git rev-parse --show-toplevel)/bin/mvn test app/example-wordcount-scala-with-unit-test/wc
