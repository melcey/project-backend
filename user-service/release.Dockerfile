# Dockerfile for the user microservice in Release configuration

# Build stage: use Maven image and Maven wrapper to build the JAR
FROM maven:3.9.9-eclipse-temurin-21 AS build-stage
WORKDIR /app
# Copy the Maven wrapper, Maven wrapper directory, pom file, and source code
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src ./src
# Ensure mvnw is executable and run the Maven build
RUN chmod +x mvnw && ./mvnw clean install

# Fetches the image for Eclipse Temurin JDK 21
FROM eclipse-temurin:21-jdk
# Sets the working directory
WORKDIR /app
# Copy the built JAR from the build stage
COPY --from=build-stage /app/target/*.jar user_service.jar
# Exposes the port 8080 for the containers which will run on top of this image
EXPOSE 8080
# Runs the command "java -jar user_service.jar" when starting the image
ENTRYPOINT ["java", "-jar", "user_service.jar"]