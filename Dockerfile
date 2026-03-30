# Use official OpenJDK 17 image
FROM eclipse-temurin:17-jdk-alpine

# Set working directory inside container
WORKDIR /app

# Copy built JAR file
COPY target/account-service-0.0.1-SNAPSHOT.jar app.jar

# Expose port (Render will map the dynamic port)
EXPOSE 8080

# Run the Spring Boot app
ENTRYPOINT ["java", "-jar", "app.jar"]