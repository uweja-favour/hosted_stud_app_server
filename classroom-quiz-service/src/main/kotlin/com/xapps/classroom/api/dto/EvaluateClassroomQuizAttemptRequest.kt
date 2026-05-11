package com.xapps.classroom.api.dto

import com.xapps.model.QuizId
import com.xapps.model.attempt.QuizAttempt
import kotlinx.serialization.Serializable

@Serializable
data class EvaluateClassroomQuizAttemptRequest(
    val quizId: QuizId,
    val sessionId: String,
    val attempt: QuizAttempt
)