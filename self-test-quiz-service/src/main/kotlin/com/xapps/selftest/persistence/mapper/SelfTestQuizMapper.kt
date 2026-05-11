package com.xapps.selftest.persistence.mapper

import com.xapps.persistence.mapper.toDocument
import com.xapps.persistence.mapper.toDomain
import com.xapps.time.types.KotlinInstant
import com.xapps.selftest.domain.model.SelfTestQuiz
import com.xapps.selftest.persistence.entity.SelfTestQuizDocument
import org.springframework.stereotype.Component

@Component
class SelfTestQuizMapper(
    private val attemptMapper: AttemptMapper
) {

    fun toDomain(doc: SelfTestQuizDocument): SelfTestQuiz {
        return SelfTestQuiz(
            id = doc.getTheId(),
            title = doc.title,
            subject = doc.subject,
            topic = doc.topic,
            description = doc.description,
            questions = doc.questions.map { it.toDomain() },
            createdAt = KotlinInstant.fromEpochMilliseconds(doc.createdAt),
            attempts = doc.attempts.map { attemptMapper.toDomain(it) }
        )
    }

    fun toDocument(domain: SelfTestQuiz): SelfTestQuizDocument {
        return SelfTestQuizDocument(
            id1 = domain.id,
            title = domain.title,
            subject = domain.subject,
            topic = domain.topic,
            description = domain.description,
            questions = domain.questions.map { it.toDocument() },
            createdAt = domain.createdAt.toEpochMilliseconds(),
            attempts = domain.attempts.map { attemptMapper.toDocument(it) }
        )
    }
}