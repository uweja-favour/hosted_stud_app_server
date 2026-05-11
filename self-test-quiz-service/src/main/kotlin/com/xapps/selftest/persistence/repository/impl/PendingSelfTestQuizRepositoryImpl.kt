package com.xapps.selftest.persistence.repository.impl

import com.xapps.model.DeliveryStatus
import com.xapps.model.QuizId
import com.xapps.selftest.domain.model.PendingSelfTestQuiz
import com.xapps.selftest.domain.repository.PendingSelfTestQuizRepository
import com.xapps.selftest.persistence.entity.PendingSelfTestQuizDocument
import com.xapps.selftest.persistence.mapper.PendingSelfTestQuizMapper
import com.xapps.selftest.persistence.repository.PendingSelfTestQuizMongoRepository
import com.xapps.selftest.persistence.saveAllUpserting
import com.xapps.selftest.persistence.saveUpserting
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.springframework.stereotype.Component

@Component
class PendingSelfTestQuizRepositoryImpl(
    private val mongoRepository: PendingSelfTestQuizMongoRepository,
    private val mapper: PendingSelfTestQuizMapper
) : PendingSelfTestQuizRepository {

    override fun findAllByUserIdAndStatus(
        userId: String,
        status: DeliveryStatus
    ): Flow<PendingSelfTestQuiz> {
        return mongoRepository.findAllByUserIdAndStatusCode(userId, status.code)
            .map { mapper.toDomain(it) }
    }

    override suspend fun findByUserIdAndQuizId(
        userId: String,
        quizId: QuizId
    ): PendingSelfTestQuiz? {
        return mongoRepository.findByUserIdAndQuizId(userId = userId, quizId = quizId)
            ?.let { mapper.toDomain(it) }
    }

    override suspend fun findByQuizId(quizId: QuizId): PendingSelfTestQuiz? {
        return mongoRepository.findByQuizId(quizId)
            ?.let { mapper.toDomain(it) }
    }

    override suspend fun save(userPendingQuiz: PendingSelfTestQuiz) {
        mongoRepository.saveUpserting(mapper.toDocument(userPendingQuiz))
    }

    override suspend fun saveAll(userPendingQuizzes: List<PendingSelfTestQuiz>) {
        mongoRepository.saveAllUpserting(userPendingQuizzes.map { mapper.toDocument(it) })
    }
}