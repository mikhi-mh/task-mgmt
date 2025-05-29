# Use an official OpenJDK 11 runtime as a base image
FROM openjdk:11-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the jar file from the target directory into the container
COPY target/mikhi-task-management-0.0.1-SNAPSHOT.jar app.jar
# check for actual name of the jar under the target folder like I did

# Expose the port your Spring Boot app will run on
EXPOSE 8080

# Command to run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
