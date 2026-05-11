package com.xapps.selftest.application.generation

import com.xapps.dto.CreateSelfTestQuizRequest
import com.xapps.dto.job.JobDTO
import com.xapps.dto.job.JobStatus
import com.xapps.dto.job.JobTask
import com.xapps.model.TaskDraftStatus
import com.xapps.platform.core.string.generateUniqueId
import com.xapps.questions.contracts.question_generation.JobId
import com.xapps.selftest.application.port.out.QuestionGenerationRequestPublisher
import com.xapps.selftest.domain.model.SelfTestQuizDraft
import com.xapps.selftest.domain.repository.SelfTestQuizDraftRepository
import org.springframework.stereotype.Component

@Component
class SelfTestQuizCreationOrchestrator(
    private val draftRepository: SelfTestQuizDraftRepository,
    private val questionRequestPublisher: QuestionGenerationRequestPublisher,
) {

    suspend fun createQuiz(
        userId: String,
        setup: CreateSelfTestQuizRequest
    ): JobDTO {

        val quizId = generateUniqueId()
        val jobId = JobId.of(generateUniqueId())

        val draft = SelfTestQuizDraft(
            id = generateUniqueId(),
            quizId = quizId,
            title = setup.title,
            subject = setup.subject,
            topic = setup.topic,
            description = setup.description,
            status = TaskDraftStatus.IN_PROGRESS
        )

        draftRepository.save(draft)

        questionRequestPublisher.requestQuestionGeneration(
            userId = userId,
            quizId = quizId,
            jobId = jobId,
            setup = setup
        )

        return JobDTO(
            jobId = jobId,
            task = JobTask.SELF_TEST,
            status = JobStatus.Queued
        )
    }
}