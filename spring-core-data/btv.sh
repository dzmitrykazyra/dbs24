#!/bin/sh
export JAVA_HOME="/usr/lib/jvm/jdk-15.0.2"
mvn -version
mvn clean install -Pprofile_test_dev clean install -DskipTests=true