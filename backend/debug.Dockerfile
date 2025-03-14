# Dockerfile for the user microservice in Debug configuration

# Build stage: use Maven image and Maven wrapper to build the JAR
FROM maven:3.9.9-eclipse-temurin-21 AS build-stage
WORKDIR /app
# Copy the Maven wrapper, Maven wrapper directory, pom file, and source code
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src ./src
# Ensure mvnw is executable and run the Maven build
# Need to skip tests; otherwise, it will try to connect to the database; due to which the build would fail
RUN chmod +x mvnw && ./mvnw clean install -D skipTests

# Fetches the image for Eclipse Temurin JDK 21
FROM eclipse-temurin:21-jdk
# Sets the working directory
WORKDIR /app
# Copy the built JAR from the build stage
COPY --from=build-stage /app/target/*.jar backend.jar
# Exposes the port 8080 for the containers which will run on top of this image,
# and the port 5005 for remote debugging
EXPOSE 8080 5005
# Starts the application with the JVM remote debugger enabled.
# Change "suspend=n" to "suspend=y" if you want the JVM to wait for the debugger before starting. (No need to do this)
ENTRYPOINT ["java", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005", "-jar", "backend.jar"]