package com.xapps.selftest.application.service

import com.xapps.model.QuizId
import com.xapps.selftest.domain.exceptions.SelfTestDomainError
import com.xapps.selftest.domain.model.SelfTestQuiz
import com.xapps.selftest.domain.repository.SelfTestQuizRepository
import org.springframework.stereotype.Component

@Component
class SelfTestQuizQueryService(
    private val repository: SelfTestQuizRepository
) {

    suspend fun getQuiz(
        quizId: QuizId
    ): SelfTestQuiz {

        val quiz = repository.findById(quizId)
            ?: throw SelfTestDomainError.InvalidSelfTestQuiz(quizId)

        return quiz
    }
}