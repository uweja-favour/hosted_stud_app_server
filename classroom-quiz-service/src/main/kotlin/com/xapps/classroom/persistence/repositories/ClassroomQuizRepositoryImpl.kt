package com.xapps.classroom.persistence.repositories

import com.xapps.classroom.domain.model.canonical__server_only.ClassroomQuiz
import com.xapps.classroom.domain.repository.ClassroomQuizRepository
import com.xapps.classroom.persistence.mappers.toDocument
import com.xapps.classroom.persistence.mappers.toDomain
import com.xapps.classroom.persistence.saveUpserting
import com.xapps.model.QuizId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.springframework.stereotype.Component

@Component("baseClassroomQuizRepository")
class ClassroomQuizRepositoryImpl(
    private val repository: ClassroomQuizMongoRepository,
) : ClassroomQuizRepository {

    override suspend fun save(classroomQuiz: ClassroomQuiz) {
        repository.saveUpserting(classroomQuiz.toDocument())
    }

    override suspend fun findById(quizId: QuizId): ClassroomQuiz? {
        return repository.findById(quizId)?.toDomain()
    }

    override fun findAll(): Flow<ClassroomQuiz> {
        return repository.findAll().map { it.toDomain() }
    }

    override suspend fun findAllByTutorId(tutorId: String): List<ClassroomQuiz> {
        return repository.findAllByTutorId(tutorId).map { it.toDomain() }
    }

    override suspend fun findBySessionId(sessionId: String): ClassroomQuiz? {
        return repository.findBySessions_Id(sessionId)?.toDomain()
    }

    override suspend fun findBySessionJoinCode(joinCode: String): ClassroomQuiz? {
        return repository.findBySessions_JoinCode(joinCode)?.toDomain()
    }

    override fun findQuizIdsByParticipantUserId(userId: String): Flow<QuizIdProjection> {
        return repository.findQuizIdsByParticipantUserId(userId)
    }

    override fun findQuizzesByParticipantUserId(userId: String): Flow<ClassroomQuiz> {
        return repository.findQuizzesByParticipantUserId(userId).map { it.toDomain() }
    }
}