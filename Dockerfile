# Use official Maven + JDK image
FROM maven:3.9.2-eclipse-temurin-17 AS build

# Set working directory
WORKDIR /app

# Copy Maven files
COPY pom.xml mvnw ./
COPY .mvn .mvn

# Download dependencies
RUN ./mvnw dependency:go-offline

# Copy source code
COPY src src

# Build the Spring Boot JAR
RUN ./mvnw clean package -DskipTests

# ======= Second Stage =======
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copy the built JAR from the build stage
COPY --from=build /app/target/account-service-0.0.1-SNAPSHOT.jar app.jar

# Expose port
EXPOSE 8080

# Run app
ENTRYPOINT ["java", "-jar", "app.jar"]