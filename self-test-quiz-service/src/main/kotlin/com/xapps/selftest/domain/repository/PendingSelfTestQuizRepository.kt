package com.xapps.selftest.domain.repository

import com.xapps.model.DeliveryStatus
import com.xapps.model.QuizId
import com.xapps.selftest.domain.model.PendingSelfTestQuiz
import kotlinx.coroutines.flow.Flow

interface PendingSelfTestQuizRepository {
    fun findAllByUserIdAndStatus(
        userId: String,
        status: DeliveryStatus = DeliveryStatus.PENDING
    ): Flow<PendingSelfTestQuiz>

    suspend fun findByUserIdAndQuizId(
        userId: String,
        quizId: QuizId
    ): PendingSelfTestQuiz?
    suspend fun findByQuizId(quizId: QuizId): PendingSelfTestQuiz?

    suspend fun save(userPendingQuiz: PendingSelfTestQuiz)
    suspend fun saveAll(userPendingQuizzes: List<PendingSelfTestQuiz>)
}