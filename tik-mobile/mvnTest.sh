export JAVA_HOME="/usr/lib/jvm/jdk-15.0.2"
mvn clean install -Dtest=org.dbs24.tik.mobile.test.functional.StressTest -Pprofile_test_dev surefire:test