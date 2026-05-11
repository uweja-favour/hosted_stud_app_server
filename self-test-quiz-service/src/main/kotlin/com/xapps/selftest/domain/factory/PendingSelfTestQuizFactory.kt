package com.xapps.selftest.domain.factory

import com.xapps.model.DeliveryStatus
import com.xapps.platform.core.string.generateUniqueId
import com.xapps.model.QuizId
import com.xapps.selftest.domain.model.PendingSelfTestQuiz
import com.xapps.time.types.KotlinInstant
import org.springframework.stereotype.Component

@Component
class PendingSelfTestQuizFactory {

    fun create(
        userId: String,
        quizId: QuizId,
        status: DeliveryStatus,
        createdAt: KotlinInstant,
    ): PendingSelfTestQuiz {
        return PendingSelfTestQuiz(
            id = generateUniqueId(),
            userId = userId,
            quizId = quizId,
            status = status,
            createdAt = createdAt,
        )
    }
}