#!/bin/bash

set -e

cd flinklabqs

# mvn clean might also do this removal, but I don't totally trust it yet
rm -rf flinklabqs/target

mvn clean package
