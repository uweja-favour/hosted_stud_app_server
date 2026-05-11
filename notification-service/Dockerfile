# Use official OpenJDK 17 image as the base
#FROM eclipse-temurin:17-jdk-alpine

# Set working directory
#WORKDIR /app

# Copy build artifacts
#COPY build/libs/classroom-quiz-service-0.0.1-SNAPSHOT.jar app.jar

# Expose the port your app listens on
#EXPOSE 8080

# Run the jar
#ENTRYPOINT ["java","-jar","app.jar"]



FROM eclipse-temurin:17-jdk AS build
WORKDIR /app
COPY . .
RUN ./gradlew clean bootJar --no-daemon

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
