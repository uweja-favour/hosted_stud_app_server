package com.xapps.selftest.domain.exceptions

import com.xapps.model.QuizId

sealed class SelfTestDomainError : RuntimeException() {

    class InvalidSelfTestQuiz(val quizId: QuizId) : SelfTestDomainError()
}