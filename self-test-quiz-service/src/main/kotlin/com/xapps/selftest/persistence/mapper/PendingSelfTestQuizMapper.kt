package com.xapps.selftest.persistence.mapper

import com.xapps.model.DeliveryStatus
import com.xapps.selftest.domain.model.PendingSelfTestQuiz
import com.xapps.selftest.persistence.entity.PendingSelfTestQuizDocument
import org.springframework.stereotype.Component

@Component
class PendingSelfTestQuizMapper {

    fun toDocument(pendingQuiz: PendingSelfTestQuiz): PendingSelfTestQuizDocument {
        return with(pendingQuiz) {
            PendingSelfTestQuizDocument(
                id1 = id,
                userId = userId,
                quizId = quizId,
                statusCode = status.code,
                createdAt = createdAt
            )
        }
    }

    fun toDomain(pendingQuizDocument: PendingSelfTestQuizDocument): PendingSelfTestQuiz {
        return with(pendingQuizDocument) {
            PendingSelfTestQuiz(
                id = getTheId(),
                userId = userId,
                quizId = quizId,
                status = DeliveryStatus.fromCode(statusCode),
                createdAt = createdAt
            )
        }
    }
}