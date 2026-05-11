package com.xapps.selftest.persistence.repository

import com.xapps.model.DeliveryStatus
import com.xapps.model.DeliveryStatusCode
import com.xapps.model.QuizId
import com.xapps.selftest.persistence.entity.PendingSelfTestQuizDocument
import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface PendingSelfTestQuizMongoRepository : CoroutineCrudRepository<PendingSelfTestQuizDocument, String> {

    fun findAllByUserIdAndStatusCode(
        userId: String,
        statusCode: DeliveryStatusCode = DeliveryStatus.PENDING.code
    ): Flow<PendingSelfTestQuizDocument>

    suspend fun findByQuizId(quizId: QuizId): PendingSelfTestQuizDocument?

    suspend fun findByUserIdAndQuizId(
        userId: String,
        quizId: QuizId
    ): PendingSelfTestQuizDocument?
}