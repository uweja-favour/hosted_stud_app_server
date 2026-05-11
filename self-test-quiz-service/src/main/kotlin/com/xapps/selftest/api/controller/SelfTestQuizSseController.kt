package com.xapps.selftest.api.controller

import com.xapps.dto.SseJobUpdateDto
import com.xapps.dto.SseJobSubscriptionRequest
import com.xapps.selftest.service.JobSseService
import kotlinx.coroutines.flow.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.http.codec.ServerSentEvent
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ServerWebExchange

@RestController
@RequestMapping("/api/self_test_quiz")
class SelfTestQuizSseController(
    private val jobSseOrchestrator: JobSseService
) {

    val logger: Logger = LoggerFactory.getLogger(javaClass)

    @PostMapping(
        "/stream",
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.TEXT_EVENT_STREAM_VALUE]
    )
    suspend fun streamJobUpdates(
        @RequestBody subscription: SseJobSubscriptionRequest,
        exchange: ServerWebExchange
    ): Flow<ServerSentEvent<SseJobUpdateDto>> {
        logger.info("SSE subscribe request: jobIds=${subscription.jobIds}")
        val authHeader = exchange.request.headers.getFirst("Authorization")
        return jobSseOrchestrator.streamJobUpdates(subscription, authHeader)
    }
}
