# Dockerfile

# Use a Gradle image to build the application
FROM gradle:jdk21 AS build

WORKDIR /app

# Copy the Gradle wrapper and the application code
COPY . .

# Give execution permissions to gradlew
RUN chmod +x gradlew

# Build the application
RUN ./gradlew clean build -x test

# Use a lightweight Java image to run the application
FROM openjdk:22-jdk-slim

WORKDIR /app

# Copy the built JAR file from the previous stage
COPY --from=build /app/build/libs/Backend-0.0.1-SNAPSHOT.jar .

ENV DATABASE_URL=jdbc:postgresql://dpg-cr87qda3esus73frqiv0-a:5432/bankdash_db
ENV DATABASE_USERNAME=bankdash_db_user
ENV DATABASE_PASSWORD=yj7ERU34mFzmspqAiJBvnkn6eWFiwamK

# Run the application
ENTRYPOINT ["java", "-jar", "Backend-0.0.1-SNAPSHOT.jar"]

