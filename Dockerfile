FROM mcr.microsoft.com/java/jdk:11u1-zulu-alpine
MAINTAINER WH
COPY kettle-scheduler-starter-1.0-SNAPSHOT.jar  /app.jar
CMD java -jar /app.jar
EXPOSE 8080

