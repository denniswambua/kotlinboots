FROM openjdk:8-jdk-alpine

ARG JAVA_OPTS
ARG JAVA_FILE

COPY $JAVA_FILE /app.jar

ENTRYPOINT ["java","-jar","/app.jar"]
