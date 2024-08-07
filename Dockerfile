# Use a specific version of OpenJDK (ensure it's available and correct)
FROM openjdk:22-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file from the build directory to the working directory inside the container
COPY build/libs/Backend-0.0.1-SNAPSHOT.jar /app/Backend.jar

# Expose port 8080 to the outside world
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "--enable-preview", "-jar", "Backend.jar"]
