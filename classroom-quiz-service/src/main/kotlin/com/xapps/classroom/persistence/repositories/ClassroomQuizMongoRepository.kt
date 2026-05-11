package com.xapps.classroom.persistence.repositories

import com.xapps.classroom.persistence.entities.ClassroomQuizDocument
import com.xapps.model.QuizId
import kotlinx.coroutines.flow.Flow
import org.springframework.data.mongodb.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

data class QuizIdProjection(
    val id1: QuizId
)

@Repository
interface ClassroomQuizMongoRepository : CoroutineCrudRepository<ClassroomQuizDocument, QuizId> {

    suspend fun findAllByTutorId(tutorId: String): List<ClassroomQuizDocument>

    suspend fun findAllByTutorEmail(tutorEmail: String): List<ClassroomQuizDocument>

    @Query("{ 'sessions.joinCode': ?0 }")
    suspend fun findBySessions_JoinCode(joinCode: String): ClassroomQuizDocument?

    @Query("{ 'sessions.id': ?0 }")
    suspend fun findBySessions_Id(sessionId: String): ClassroomQuizDocument?

    @Query("{ 'sessions.participations.userId': ?0 }")
    fun findQuizzesByParticipantUserId(userId: String): Flow<ClassroomQuizDocument>

    @Query(
        value = "{ 'sessions.participations.userId': ?0 }",
        fields = "{ '_id': 1 }"
    )
    fun findQuizIdsByParticipantUserId(userId: String): Flow<QuizIdProjection>

//    suspend fun deleteAllByTutorId(tutorId: String)
}