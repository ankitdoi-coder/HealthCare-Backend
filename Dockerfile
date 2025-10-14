# Use an official OpenJDK 17 runtime as a parent image
FROM eclipse-temurin:17-jdk-jammy

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven wrapper and pom.xml to leverage Docker layer caching
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

# Download project dependencies
RUN ./mvnw dependency:go-offline

# Copy the rest of the project source code
COPY src ./src

# Build the application into a .jar file
RUN ./mvnw package -DskipTests

# Expose port 8080 so Render can access the application
EXPOSE 8080

# The command to run when the container starts
ENTRYPOINT ["java", "-jar", "target/HealthCare-Backend-0.0.1-SNAPSHOT.jar"]