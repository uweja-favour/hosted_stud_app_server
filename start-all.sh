#!/bin/bash
java -jar auth-service.jar               --spring.profiles.active=dev --server.port=8080 &
java -jar classroom-quiz-service.jar     --spring.profiles.active=dev --server.port=8081 &
java -jar note-summary-service.jar       --spring.profiles.active=dev --server.port=8082 &
java -jar question-generator-service.jar --spring.profiles.active=dev --server.port=8083 &
java -jar realtime-gateway.jar           --spring.profiles.active=dev --server.port=8084 &
java -jar self-test-quiz-service.jar     --spring.profiles.active=dev --server.port=8085 &

wait -n
exit $?