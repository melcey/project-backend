# Fetches the image for Eclipse Temurin JDK 21
FROM eclipse-temurin:21-jdk
# Sets the working directory
WORKDIR /app
# Adds the compiled jar file into the working directory for the image
ADD target/*.jar gateway.jar
# Exposes the port 8080 for the containers which will run on top of this image
EXPOSE 8080
# Runs the command "java -jar gateway.jar" when starting the image
ENTRYPOINT ["java", "-jar", "gateway.jar"]