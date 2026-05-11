package com.xapps.classroom.application.participant

import com.xapps.classroom.domain.exceptions.ClassroomDomainError
import com.xapps.classroom.domain.extensions.mutateAndBump
import com.xapps.classroom.domain.model.canonical__server_only.ensureSubmissionWindowOpen
import com.xapps.classroom.domain.model.mappers.toParticipantModel
import com.xapps.classroom.domain.model.participant.ParticipantClassroomQuiz
import com.xapps.classroom.domain.repository.ClassroomQuizRepository
import com.xapps.classroom.domain.service.evaluation.QuizEvaluator
import com.xapps.model.QuizId
import com.xapps.model.attempt.AttemptState
import com.xapps.model.attempt.QuizAttempt
import com.xapps.time.clock.ClockProvider
import com.xapps.time.types.KotlinInstant
import org.springframework.stereotype.Component

@Component
class QuizAttemptSubmissionOrchestrator(
    private val repository: ClassroomQuizRepository,
    private val evaluator: QuizEvaluator,
    private val clockProvider: ClockProvider
) {

    suspend fun evaluateAttempt(
        userId: String,
        quizId: QuizId,
        sessionId: String,
        attempt: QuizAttempt
    ): ParticipantClassroomQuiz {

        val quiz = repository.findById(quizId)
            ?: throw ClassroomDomainError.ClassroomQuizNotFound(quizId)

        val session = quiz.sessions.find { it.id == sessionId }
            ?: throw ClassroomDomainError.SessionNotFound(sessionId)

        session.ensureSubmissionWindowOpen(now = clockProvider.now())

        val participation = session.participations
            .find { it.userId == userId }
            ?: throw ClassroomDomainError.ParticipantNotFound()

        val evaluatedAttempt = attempt.copy(
            state = AttemptState.Evaluated(
                startedAt = extractStartedAt(attempt),
                submittedAt = clockProvider.now(),
                activeDurationMillis = extractActiveDuration(attempt),
                evaluation = evaluator.evaluate(
                    attemptId = attempt.id,
                    answers = attempt.answers,
                    questions = quiz.questions
                ),
                evaluatedAt = clockProvider.now()
            )
        )

        val updatedParticipation = participation.copy(attempt = evaluatedAttempt)

        val updatedQuiz = quiz.mutateAndBump { current ->

            val updatedSession = session.copy(
                participations = session.participations.map {
                    if (it.userId == userId) updatedParticipation else it
                }
            )

            current.copy(
                sessions = current.sessions.map {
                    if (it.id == session.id) updatedSession else it
                }
            )
        }

        repository.save(updatedQuiz)

        return updatedQuiz.toParticipantModel(userId)
    }

    private fun extractStartedAt(attempt: QuizAttempt): KotlinInstant {
        return when (val state = attempt.state) {
            is AttemptState.OnGoing -> state.startedAt
            is AttemptState.Paused -> state.startedAt
            is AttemptState.AwaitingEvaluation -> state.startedAt
            is AttemptState.Evaluated -> state.startedAt
            else -> throw ClassroomDomainError.InvalidAttemptState()
        }
    }

    private fun extractActiveDuration(attempt: QuizAttempt): Long {
        return when (val state = attempt.state) {
            is AttemptState.OnGoing -> state.activeDurationMillis
            is AttemptState.Paused -> state.activeDurationMillis
            is AttemptState.AwaitingEvaluation -> state.activeDurationMillis
            is AttemptState.Evaluated -> state.activeDurationMillis
            else -> 0L
        }
    }
}