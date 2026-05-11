package com.xapps.selftest.api.exception

import com.xapps.dto.ApiErrorResponse
import com.xapps.dto.ApiErrorResponseType
import com.xapps.selftest.domain.exceptions.SelfTestDomainError
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class SelfTestExceptionHandler {

    private val log = LoggerFactory.getLogger(javaClass)

    @ExceptionHandler(SelfTestDomainError::class)
    fun handle(ex: SelfTestDomainError): ResponseEntity<ApiErrorResponse> {

        log.error("Handling Self Test exception: ${ex.toString()}")

        val response = when (ex) {
            is SelfTestDomainError.InvalidSelfTestQuiz ->
                ApiErrorResponse(ApiErrorResponseType.SELF_TEST_QUIZ_NOT_FOUND, "Invalid self test quiz: ${ex.quizId}")
        }

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(response)
    }
}