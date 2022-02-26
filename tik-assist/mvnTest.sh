export JAVA_HOME="/usr/lib/jvm/jdk-15.0.2"
mvn clean install -Dtest=org.dbs24.tik.assist.test.v2.SumResolverTests -Pprofile_test_dev surefire:test