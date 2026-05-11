package com.xapps.selftest.application.port.out

import com.xapps.questions.contracts.question_generation.JobId
import com.xapps.dto.CreateSelfTestQuizRequest

interface QuestionGenerationRequestPublisher {
    suspend fun requestQuestionGeneration(
        userId: String,
        quizId: String,
        jobId: JobId,
        setup: CreateSelfTestQuizRequest
    )
}

