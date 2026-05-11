package com.xapps.selftest.service.sse

import com.xapps.dto.SseJobUpdateDto
import com.xapps.dto.job.isFinal
import com.xapps.questions.contracts.question_generation.JobId
import kotlinx.atomicfu.atomic
import kotlinx.atomicfu.update
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.launch
import org.slf4j.Logger
import org.springframework.http.codec.ServerSentEvent

internal class JobSseSession(
    jobIds: Set<JobId>,
    private val logger: Logger,
    private val scope: ProducerScope<ServerSentEvent<SseJobUpdateDto>>
) {

    private val activeJobs = atomic(jobIds)

    fun start() {
        scope.launch {
            scope.send(comment("Connected to jobs: ${activeJobs.value.joinToString()}"))
        }
    }

    suspend fun onUpdate(update: SseJobUpdateDto) {
        scope.send(update.toEvent())

        if (update.status.isFinal()) {
            activeJobs.update { it - update.jobId }
            logger.info(
                "Job ${update.jobId} completed; remaining=${activeJobs.value.size}"
            )
        }

        if (activeJobs.value.isEmpty()) {
            logger.info("All jobs completed; closing SSE session")
            scope.close()
        }
    }

    private fun SseJobUpdateDto.toEvent(): ServerSentEvent<SseJobUpdateDto> =
        ServerSentEvent.builder(this)
            .id(jobId.value)
            .event("job-update")
            .data(this)
            .build()

    private fun comment(text: String): ServerSentEvent<SseJobUpdateDto> =
        ServerSentEvent.builder<SseJobUpdateDto>()
            .comment(text)
            .build()
}
