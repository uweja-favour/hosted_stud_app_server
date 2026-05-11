package com.xapps.classroom.application.repository

import com.xapps.classroom.application.change.ClassroomQuizChangeDetector
import com.xapps.classroom.application.port.out.PublishParticipantQuizStateRefreshEventPort
import com.xapps.classroom.application.port.out.PublishTutorQuizStateRefreshEventPort
import com.xapps.classroom.domain.model.canonical__server_only.ClassroomQuiz
import com.xapps.classroom.domain.repository.ClassroomQuizRepository
import com.xapps.classroom.persistence.repositories.QuizIdProjection
import com.xapps.model.QuizId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

class NotifyingClassroomQuizRepository(
    private val delegate: ClassroomQuizRepository,
    private val changeDetector: ClassroomQuizChangeDetector,
    private val tutorPublisher: PublishTutorQuizStateRefreshEventPort,
    private val participantPublisher: PublishParticipantQuizStateRefreshEventPort
) : ClassroomQuizRepository {

    override suspend fun save(classroomQuiz: ClassroomQuiz) = supervisorScope {

        val existing = delegate.findById(classroomQuiz.id)

        delegate.save(classroomQuiz)

        val shouldNotify = changeDetector.hasMeaningfulChange(existing, classroomQuiz)

        if (shouldNotify) {
            launch {
                tutorPublisher.publishTutorQuizStateRefreshEvent(
                    tutorId = classroomQuiz.tutorId,
                    quizIds = listOf(classroomQuiz.id)
                )
            }

            launch {
                participantPublisher.publishQuizRefreshParticipantEvent(
                    quizId = classroomQuiz.id,
                    participantIds = classroomQuiz.sessions.flatMap { session ->
                        session.participations.map { it.userId }
                    }
                )
            }
        }
    }

    override suspend fun findById(quizId: QuizId): ClassroomQuiz? =
        delegate.findById(quizId)

    override fun findAll(): Flow<ClassroomQuiz> =
        delegate.findAll()

    override suspend fun findAllByTutorId(tutorId: String): List<ClassroomQuiz> =
        delegate.findAllByTutorId(tutorId)

    override suspend fun findBySessionId(sessionId: String): ClassroomQuiz? =
        delegate.findBySessionId(sessionId)

    override suspend fun findBySessionJoinCode(joinCode: String): ClassroomQuiz? =
        delegate.findBySessionJoinCode(joinCode)

    override fun findQuizIdsByParticipantUserId(userId: String): Flow<QuizIdProjection> =
        delegate.findQuizIdsByParticipantUserId(userId)

    override fun findQuizzesByParticipantUserId(userId: String): Flow<ClassroomQuiz> =
        delegate.findQuizzesByParticipantUserId(userId)

}