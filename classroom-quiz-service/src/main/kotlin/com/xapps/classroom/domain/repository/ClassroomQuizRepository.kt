package com.xapps.classroom.domain.repository

import com.xapps.classroom.domain.model.canonical__server_only.ClassroomQuiz
import com.xapps.classroom.persistence.repositories.QuizIdProjection
import com.xapps.model.QuizId
import kotlinx.coroutines.flow.Flow

interface ClassroomQuizRepository {
    suspend fun save(classroomQuiz: ClassroomQuiz)
    suspend fun findById(quizId: QuizId): ClassroomQuiz?
    fun findAll(): Flow<ClassroomQuiz>
    suspend fun findAllByTutorId(tutorId: String): List<ClassroomQuiz>
    suspend fun findBySessionId(sessionId: String): ClassroomQuiz?
    suspend fun findBySessionJoinCode(joinCode: String): ClassroomQuiz?
    fun findQuizIdsByParticipantUserId(userId: String): Flow<QuizIdProjection>
    fun findQuizzesByParticipantUserId(userId: String): Flow<ClassroomQuiz>
}