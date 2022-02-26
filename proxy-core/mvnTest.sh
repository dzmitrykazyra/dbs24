export JAVA_HOME="/usr/lib/jvm/jdk-15.0.2"
mvn clean install -Dtest=org.dbs24.test.BookProxiesTests -Pprofile_test_dev surefire:test