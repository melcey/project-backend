# Use Maven image for building and running tests
FROM maven:3.9.9-eclipse-temurin-21
# Set working directory
WORKDIR /app
# Copy the Maven wrapper, Maven wrapper directory, pom file, and source code
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src ./src

# Make Maven wrapper executable
RUN chmod +x mvnw

# Command to run tests when container starts
ENTRYPOINT ["./mvnw", "clean", "test"]