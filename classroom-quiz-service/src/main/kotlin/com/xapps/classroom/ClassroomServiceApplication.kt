package com.xapps.classroom

import com.xapps.platform.core.time.nowInKotlinInstant
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(
    exclude = [
        JacksonAutoConfiguration::class,
        org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration::class,
    ]
)
class ClassroomServiceApplication

fun main(args: Array<String>) {
    nowInKotlinInstant()
    runApplication<ClassroomServiceApplication>(*args)
}

// RUN WITH DEV PROFILE
// ./gradlew :classroom-service:bootRun --args='--spring.profiles.active=dev'   WRONG


// ./gradlew :classroom-quiz-service:bootRun --args='--spring.profiles.active=dev'


// ./gradlew :classroom-service:bootRun