# Stage 1: Build the project using Maven
FROM maven:3.9.6-eclipse-temurin-17 AS build

# Set working directory inside container
WORKDIR /app

# Copy only the demo/ folder contents into /app
COPY demo/ ./

# Build the Spring Boot jar, skipping tests
RUN mvn clean package -DskipTests

# Stage 2: Run the app using lightweight Java image
FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

# Copy the built jar from the first stage
COPY --from=build /app/target/*.jar app.jar

# Expose port 8080
EXPOSE 8080

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
