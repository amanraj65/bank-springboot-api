FROM eclipse-temurin:23-jdk-alpine
WORKDIR /app

# Copy source code
COPY . .

# Build the project
RUN ./mvnw clean package -DskipTests

# Run the app using the generated JAR
ENTRYPOINT ["java","-jar","target/account-service-0.0.1-SNAPSHOT.jar"]

# Expose default Render port
EXPOSE 8080