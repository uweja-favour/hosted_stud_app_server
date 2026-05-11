package com.xapps.selftest.service

import com.xapps.dto.SseJobSubscriptionRequest
import com.xapps.dto.SseJobUpdateDto
import com.xapps.selftest.infrastructure.client.JobUpdateClient
import com.xapps.selftest.service.sse.JobSseSession
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.codec.ServerSentEvent
import org.springframework.stereotype.Service

@OptIn(ExperimentalCoroutinesApi::class)
@Service
class JobSseService(
    private val jobUpdateClient: JobUpdateClient
) {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    fun streamJobUpdates(
        subscription: SseJobSubscriptionRequest,
        authHeader: String?
    ): Flow<ServerSentEvent<SseJobUpdateDto>> =
        channelFlow {

            if (subscription.jobIds.isEmpty()) {
                logger.info("No jobIds provided; returning empty stream")
                close()
                return@channelFlow
            }

            val session = JobSseSession(
                jobIds = subscription.jobIds,
                logger = logger,
                scope = this
            )

            session.start()

            jobUpdateClient
                .streamUpdates(subscription, authHeader)
                .flowOn(Dispatchers.Default)
                .catch { e ->
                    logger.error("Error streaming job updates", e)
                }
                .collect(session::onUpdate)

            awaitClose {
                logger.info("SSE stream closed for jobs=${subscription.jobIds}")
            }
        }
}
