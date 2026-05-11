package com.xapps.classroom.persistence.repositories

import com.xapps.classroom.domain.model.canonical__server_only.TutorPendingClassroomQuiz
import com.xapps.classroom.domain.repository.TutorPendingClassroomQuizRepository
import com.xapps.classroom.persistence.entities.TutorPendingClassroomQuizDocument
import com.xapps.classroom.persistence.saveAllUpserting
import com.xapps.classroom.persistence.saveUpserting
import com.xapps.model.DeliveryStatus
import com.xapps.model.QuizId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.springframework.stereotype.Component

@Component
class TutorPendingClassroomQuizRepositoryImpl(
    private val mongoRepository: TutorPendingClassroomQuizMongoRepository
) : TutorPendingClassroomQuizRepository {

    override fun findAllByUserIdAndStatus(
        userId: String,
        status: DeliveryStatus
    ): Flow<TutorPendingClassroomQuiz> {
        return mongoRepository.findAllByUserIdAndStatusCode(userId, status.code)
            .map { toDomain(it) }
    }

    override suspend fun findByUserIdAndQuizId(
        userId: String,
        quizId: QuizId
    ): TutorPendingClassroomQuiz? {
       return  mongoRepository.findByUserIdAndQuizId(userId, quizId)
            ?.let { toDomain(it) }
    }

    override suspend fun findByQuizId(quizId: QuizId): TutorPendingClassroomQuiz? =
        mongoRepository.findByQuizId(quizId)
                ?.let { toDomain(it) }

    override suspend fun save(pendingTutorClassroomQuiz: TutorPendingClassroomQuiz) =
        mongoRepository.saveUpserting(toEntity(pendingTutorClassroomQuiz))

    override suspend fun saveAll(pendingTutorClassroomQuizzes: List<TutorPendingClassroomQuiz>) =
        mongoRepository.saveAllUpserting(pendingTutorClassroomQuizzes.map { toEntity(it) })

    private companion object {
        fun toEntity(pendingQuiz: TutorPendingClassroomQuiz): TutorPendingClassroomQuizDocument {
            return with(pendingQuiz) {
                TutorPendingClassroomQuizDocument(
                    id1 = id,
                    userId = userId,
                    quizId = quizId,
                    statusCode = status.code,
                    createdAt = createdAt
                )
            }
        }

        fun toDomain(pendingQuizDocument: TutorPendingClassroomQuizDocument): TutorPendingClassroomQuiz {
            return with(pendingQuizDocument) {
                TutorPendingClassroomQuiz(
                    id = getTheId(),
                    userId = userId,
                    quizId = quizId,
                    status = DeliveryStatus.fromCode(statusCode),
                    createdAt = createdAt
                )
            }
        }
    }
}