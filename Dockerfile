# ================================
# Stage 1: Build
# ================================
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

# Copy Gradle wrapper and config (cache dependencies faster)
COPY gradlew ./
COPY gradle ./gradle
COPY build.gradle settings.gradle ./

# Download dependencies
RUN ./gradlew dependencies --no-daemon || return 0

# Copy full source code
COPY . .

# Build Spring Boot app (skip tests for faster deploy)
RUN ./gradlew clean build -x test --no-daemon

# ================================
# Stage 2: Run
# ================================
FROM eclipse-temurin:21-jdk
WORKDIR /app

# Copy built JAR from build stage
COPY --from=build /app/build/libs/*.jar app.jar

# Expose port 8080 for Render
EXPOSE 8080

# Start the application
ENTRYPOINT ["java", "-jar", "app.jar"]