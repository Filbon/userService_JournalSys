# Use a base image with JDK for running Spring Boot
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the built JAR file from the target directory into the container
COPY /target/userservice-0.0.1-SNAPSHOT.jar userService.jar

# Expose the port the app runs on
EXPOSE 8082

# Run the JAR file
ENTRYPOINT ["java", "-jar", "userService.jar"]
