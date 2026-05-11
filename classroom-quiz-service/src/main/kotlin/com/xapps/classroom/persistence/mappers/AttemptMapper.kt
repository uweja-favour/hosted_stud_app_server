package com.xapps.classroom.persistence.mappers

import com.xapps.model.attempt.QuizAttempt
import com.xapps.persistence.attempt.QuizAttemptDocument
import com.xapps.persistence.mapper.toDocument
import com.xapps.persistence.mapper.toDomain

fun QuizAttempt.toDocument(): QuizAttemptDocument {
    return QuizAttemptDocument(
        id = id,
        quizId = parentId,
        attemptNumber = attemptNumber,
        answers = answers.map { it.toDocument() },
        state = state.toDocument(),
    )
}

fun QuizAttemptDocument.toDomain(): QuizAttempt {
    return QuizAttempt(
        id = id,
        parentId = quizId,
        attemptNumber = attemptNumber,
        answers = answers.map { it.toDomain() },
        state = state.toDomain(),
    )
}