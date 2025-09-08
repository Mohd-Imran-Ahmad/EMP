# Use OpenJDK 17 base image (Alpine for smaller image size)
FROM openjdk:17-jdk-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the built JAR file into the container
COPY target/ems.jar app.jar

# Expose the port your Spring Boot application runs on
EXPOSE 8081

# Define the command to run the JAR
ENTRYPOINT ["java", "-jar", "app.jar"]