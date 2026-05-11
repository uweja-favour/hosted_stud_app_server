package com.xapps.classroom.application.tutor

import com.xapps.classroom.api.dto.CreateClassroomQuizSessionRequest
import com.xapps.classroom.domain.exceptions.ClassroomDomainError
import com.xapps.classroom.domain.extensions.mutateAndBump
import com.xapps.classroom.domain.model.ClassroomSessionState
import com.xapps.classroom.domain.model.canonical__server_only.ClassroomSession
import com.xapps.classroom.domain.model.canonical__server_only.factory.ClassroomSessionFactory
import com.xapps.classroom.domain.repository.ClassroomQuizRepository
import com.xapps.time.clock.ClockProvider
import com.xapps.time.types.KotlinInstant
import org.springframework.stereotype.Component

@Component
class TutorQuizSessionService(
    private val repository: ClassroomQuizRepository,
    private val clockProvider: ClockProvider
) {

    suspend fun createSession(request: CreateClassroomQuizSessionRequest) {

        val quiz = repository.findById(request.quizId)
            ?: throw ClassroomDomainError.ClassroomQuizNotFound(quizId = request.quizId)

        val hasActiveSession = quiz.sessions.any {
            it.timeline().resolveState(clockProvider.now()) !is ClassroomSessionState.Closed
        }

        if (hasActiveSession) {
            throw ClassroomDomainError.ActiveSessionAlreadyExists(request.quizId)
        }

        val newSession = newSession(request, clockProvider.now())
        val updatedQuiz = quiz.mutateAndBump {
            it.copy(
                sessions = quiz.sessions + newSession
            )
        }

        repository.save(updatedQuiz)
    }

    private fun newSession(
        request: CreateClassroomQuizSessionRequest,
        now: KotlinInstant
    ): ClassroomSession {
        return with(request) {
            ClassroomSessionFactory.create(
                quizId = quizId,
                now = now,
                startTime = startTime,
                duration = duration,
                submissionGraceDuration = submissionGraceDuration,
                maxParticipants = maxParticipants
            )
        }
    }
}