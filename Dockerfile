# Use Java 17 base image
FROM openjdk:17-oracle as build

LABEL authors="JulianBroudy"

VOLUME /tmp
EXPOSE 8080

ARG JAR_FILE=target/*.jar
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]