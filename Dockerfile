# ─────────────────────────────────────────────
# 1. BUILD STAGE
# ─────────────────────────────────────────────
FROM gradle:8.7-jdk21 AS build

WORKDIR /app

COPY . .

RUN ./gradlew \
      :auth-service:bootJar \
      :classroom-quiz-service:bootJar \
      :note-summary-service:bootJar \
      :question-generator-service:bootJar \
      :realtime-gateway:bootJar \
      :self-test-quiz-service:bootJar \
      --no-daemon

# ─────────────────────────────────────────────
# 2. RUNTIME STAGE
# ─────────────────────────────────────────────
FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /app/auth-service/build/libs/*.jar                  auth-service.jar
COPY --from=build /app/classroom-quiz-service/build/libs/*.jar        classroom-quiz-service.jar
COPY --from=build /app/note-summary-service/build/libs/*.jar          note-summary-service.jar
COPY --from=build /app/question-generator-service/build/libs/*.jar    question-generator-service.jar
COPY --from=build /app/realtime-gateway/build/libs/*.jar              realtime-gateway.jar
COPY --from=build /app/self-test-quiz-service/build/libs/*.jar        self-test-quiz-service.jar

COPY start-all.sh .
RUN chmod +x start-all.sh

EXPOSE 8080

CMD ["./start-all.sh"]