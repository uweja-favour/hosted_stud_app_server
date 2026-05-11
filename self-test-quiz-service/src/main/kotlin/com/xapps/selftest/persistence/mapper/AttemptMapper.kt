package com.xapps.selftest.persistence.mapper

import com.xapps.model.attempt.QuizAttempt
import com.xapps.persistence.attempt.QuizAttemptDocument
import com.xapps.persistence.mapper.toDocument
import com.xapps.persistence.mapper.toDomain
import org.springframework.stereotype.Component

@Component
class AttemptMapper {

    fun toDocument(quizAttempt: QuizAttempt): QuizAttemptDocument {
        return with(quizAttempt) {
            QuizAttemptDocument(
                id = id,
                quizId = parentId,
                attemptNumber = attemptNumber,
                answers = answers.map { it.toDocument() },
                state = state.toDocument()
            )
        }
    }

    fun toDomain(quizAttemptDocument: QuizAttemptDocument): QuizAttempt {
        return with(quizAttemptDocument) {
            QuizAttempt(
                id = id,
                parentId = quizId,
                attemptNumber = attemptNumber,
                answers = answers.map { it.toDomain() },
                state = state.toDomain(),
            )
        }
    }
}