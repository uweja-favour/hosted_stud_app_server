package com.xapps.classroom.application.participant

import com.xapps.classroom.api.dto.SessionPreviewDTO
import com.xapps.classroom.domain.exceptions.ClassroomDomainError
import com.xapps.classroom.domain.extensions.mutateAndBump
import com.xapps.classroom.domain.model.canonical__server_only.ClassroomQuiz
import com.xapps.classroom.domain.model.canonical__server_only.SessionParticipation
import com.xapps.classroom.domain.model.canonical__server_only.ensureEnrollmentAllowed
import com.xapps.classroom.domain.model.canonical__server_only.ensurePreviewAllowed
import com.xapps.classroom.domain.model.mappers.toParticipantModel
import com.xapps.classroom.domain.model.participant.ParticipantClassroomQuiz
import com.xapps.classroom.domain.repository.ClassroomQuizRepository
import com.xapps.platform.core.string.generateUniqueId
import com.xapps.time.clock.ClockProvider
import org.springframework.stereotype.Component

@Component
class ParticipantClassroomQuizOrchestrator(
    private val classroomQuizRepository: ClassroomQuizRepository,
    private val clockProvider: ClockProvider
) {

    suspend fun previewSession(joinCode: String): SessionPreviewDTO {

        val quiz: ClassroomQuiz = classroomQuizRepository
            .findBySessionJoinCode(joinCode)
            ?: throw ClassroomDomainError.InvalidJoinCode(joinCode)

        val session = quiz.sessions.find { it.joinCode == joinCode }
            ?: throw ClassroomDomainError.InvalidJoinCode(joinCode)

        session.ensurePreviewAllowed(now = clockProvider.now())

        return SessionPreviewDTO(
            quizId = quiz.id,
            title = quiz.title,
            subject = quiz.subject,
            topic = quiz.topic,
            description = quiz.description,

            sessionId = session.id,
            createdAt = session.createdAt,
            startTime = session.startTime,
            duration = session.duration,
            submissionGraceDuration = session.submissionGraceDuration,

            maxParticipants = session.maxParticipants,
            currentParticipants = session.participations.size
        )
    }

    suspend fun enrollParticipant(
        userId: String,
        email: String,
        joinCode: String
    ): ParticipantClassroomQuiz {

        val quiz: ClassroomQuiz = classroomQuizRepository
            .findBySessionJoinCode(joinCode)
            ?: throw ClassroomDomainError.InvalidJoinCode(joinCode)

        val session = quiz.sessions.find { it.joinCode == joinCode }
            ?: throw ClassroomDomainError.InvalidJoinCode(joinCode)

        session.ensureEnrollmentAllowed(now = clockProvider.now())

        if (session.participations.any { it.userId == userId })
            throw ClassroomDomainError.ParticipantAlreadyEnrolled()

        if (session.participations.size == session.maxParticipants)
            throw ClassroomDomainError.SessionCapacityReached()

        val participation = SessionParticipation(
            id = generateUniqueId(),
            sessionId = session.id,
            userId = userId,
            email = email,
            joinedAt = clockProvider.now(),
            attempt = null
        )

        val updatedQuiz = quiz.mutateAndBump { current ->

            val updatedSession = session.copy(
                participations = session.participations + participation
            )

            current.copy(
                sessions = current.sessions.map {
                    if (it.id == session.id) updatedSession else it
                }
            )
        }

        classroomQuizRepository.save(updatedQuiz)

        return updatedQuiz.toParticipantModel(userId = userId)
    }
}