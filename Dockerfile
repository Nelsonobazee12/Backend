# Use a build stage to compile the project
FROM gradle:jdk21 AS build

WORKDIR /app
COPY . .

# Build the application
RUN ./gradlew clean build -x test

# Use a lightweight Java image to run the application
FROM openjdk:22-jdk-slim

WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/build/libs/Backend-0.0.1-SNAPSHOT.jar /app/Backend.jar

# Expose port 8080
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "Backend.jar"]
