package com.xapps.selftest

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(
    exclude = [
        JacksonAutoConfiguration::class,
        org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration::class,
//        org.springframework.boot.autoconfigure.r2dbc.R2dbcDataAutoConfiguration::class,
//        org.springframework.boot.autoconfigure.r2dbc.R2dbcConnectionDetailsAutoConfiguration::class
    ]
)
class SelfTestServiceApplication

fun main(args: Array<String>) {
	runApplication<SelfTestServiceApplication>(*args)
}

// RUN WITH DEV PROFILE
// ./gradlew :self-test-quiz-service:bootRun --args='--spring.profiles.active=dev'


// ./gradlew :self-test-quiz-service:bootRun

// 1. Make Gpt Service BLAZING FAST & (Easy to Read and Functional)
// 2. Make Text Extraction Service BLAZING FAST & (Easy to Read and Functional)
// 3. Make Prompt Builder Service DETERMINISTIC. It MUST fulfill user expectations
// 4. Ensure ALL failure paths are GUARDED. Ensure to persist job updates from a SINGLE SOURCE OF TRUTH
//    Upon a Transient Failure, the Job Should be Rescheduled after a delay of 2.5 minutes
// 5. Create the FULL Retry & Cancel Path for Jobs.

// Jobs that have reached 50% (or have returned from a gpt call) MUST NOT BE ALLOWED to be cancelled.
// The cancellation of a job that has not reached this threshold MUST BE DETERMINISTIC.




// A class may orchestrate many things or implement one thing, NEVER BOTH.
// Your class should either:
//
//Be a workflow conductor (this case), OR
//
//Be a rule executor