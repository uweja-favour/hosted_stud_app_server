package com.xapps.classroom.application.port.out

import com.xapps.classroom.api.dto.CreateClassroomQuizRequest
import com.xapps.questions.contracts.question_generation.JobId

interface QuestionGenerationRequestPublisher {
    suspend fun requestQuestionGeneration(
        tutorId: String,
        quizId: String,
        jobId: JobId,
        setup: CreateClassroomQuizRequest
    )
}

