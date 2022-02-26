#FROM openjdk:8-jdk-alpine
FROM openjdk:17-jdk-alpine
#FROM maven:3.6.3-openjdk-8 as build
ARG JAR_FILE=target/wa_monitoring.jar
ARG CONF_FILE=src/main/resources/*.*
COPY ${JAR_FILE} wa_monitoring.jar
COPY ${CONF_FILE} /

ENTRYPOINT ["java","-Dlog4j.debug=false", "-Dlog4j.configurationFile=log4j2.xml", "-jar","./wa_monitoring.jar", "--server.port=8045"]
