package com.xapps.selftest.application.port.out

import com.xapps.model.QuizId

interface PublishQuizGeneratedEventPort {
    fun publishQuizGeneratedEvent(
        userId: String,
        generatedQuizIds: List<QuizId>
    )
}