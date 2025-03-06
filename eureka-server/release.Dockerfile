# Dockerfile for the Eureka server in Release configuration

# Fetches the image for Eclipse Temurin JDK 21
FROM eclipse-temurin:21-jdk
# Sets the working directory
WORKDIR /app
# Adds the compiled jar file into the working directory for the image
ADD target/*.jar eureka_server.jar
# Exposes the port 8761 for the containers which will run on top of this image
EXPOSE 8761
# Runs the command "java -jar eureka_server.jar" when starting the image
ENTRYPOINT ["java", "-jar", "eureka_server.jar"]