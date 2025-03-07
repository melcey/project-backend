# Dockerfile for the user microservice in Debug configuration

# Fetches the image for Eclipse Temurin JDK 21
FROM eclipse-temurin:21-jdk
# Sets the working directory
WORKDIR /app
# Adds the compiled jar file into the working directory for the image
ADD target/*.jar user_service.jar
# Exposes the port 8080 for the containers which will run on top of this image,
# and the port 5005 for remote debugging
EXPOSE 8080 5005

# Starts the application with the JVM remote debugger enabled.
# Change "suspend=n" to "suspend=y" if you want the JVM to wait for the debugger before starting. (No need to do this)
ENTRYPOINT ["java", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005", "-jar", "user_service.jar"]