# Use lightweight OpenJDK 23
FROM eclipse-temurin:23-jdk-alpine

# Set working directory
WORKDIR /app

# Copy the built JAR into the container
COPY target/account-service-0.0.1-SNAPSHOT.jar app.jar

# Expose the port your app will run on (default 8080 for Render)
EXPOSE 8080

# Command to run the app
ENTRYPOINT ["java","-jar","app.jar"]