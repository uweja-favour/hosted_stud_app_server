package com.xapps.classroom.domain.repository

import com.xapps.classroom.domain.model.canonical__server_only.TutorPendingClassroomQuiz
import com.xapps.model.DeliveryStatus
import com.xapps.model.QuizId
import kotlinx.coroutines.flow.Flow

interface TutorPendingClassroomQuizRepository {
    fun findAllByUserIdAndStatus(
        userId: String,
        status: DeliveryStatus = DeliveryStatus.PENDING
    ): Flow<TutorPendingClassroomQuiz>

    suspend fun findByUserIdAndQuizId(
        userId: String,
        quizId: QuizId
    ): TutorPendingClassroomQuiz?
    suspend fun findByQuizId(quizId: QuizId): TutorPendingClassroomQuiz?

    suspend fun save(pendingTutorClassroomQuiz: TutorPendingClassroomQuiz)
    suspend fun saveAll(pendingTutorClassroomQuizzes: List<TutorPendingClassroomQuiz>)
}