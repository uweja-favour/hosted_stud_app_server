# ─────────────────────────────────────────────
# 1. BUILD STAGE
# ─────────────────────────────────────────────
FROM gradle:8.7-jdk21 AS build

WORKDIR /app

COPY . .

RUN ./gradlew :self-test-quiz-service:bootJar --no-daemon


# ─────────────────────────────────────────────
# 2. RUNTIME STAGE
# ─────────────────────────────────────────────
FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /app/self-test-quiz-service/build/libs/*.jar app.jar

EXPOSE 8080

# FORCE dev profile here
ENTRYPOINT ["sh", "-c", "java -jar app.jar --server.port=8080 --spring.profiles.active=dev"]