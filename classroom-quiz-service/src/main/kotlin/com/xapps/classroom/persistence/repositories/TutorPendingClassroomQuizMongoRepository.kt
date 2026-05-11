package com.xapps.classroom.persistence.repositories

import com.xapps.classroom.persistence.entities.TutorPendingClassroomQuizDocument
import com.xapps.model.DeliveryStatus
import com.xapps.model.DeliveryStatusCode
import com.xapps.model.QuizId
import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface TutorPendingClassroomQuizMongoRepository : CoroutineCrudRepository<TutorPendingClassroomQuizDocument, String> {

    fun findAllByUserIdAndStatusCode(
        userId: String,
        statusCode: DeliveryStatusCode = DeliveryStatus.PENDING.code
    ): Flow<TutorPendingClassroomQuizDocument>

    suspend fun findByQuizId(quizId: QuizId): TutorPendingClassroomQuizDocument?

    suspend fun findByUserIdAndQuizId(
        userId: String,
        quizId: QuizId
    ): TutorPendingClassroomQuizDocument?
}