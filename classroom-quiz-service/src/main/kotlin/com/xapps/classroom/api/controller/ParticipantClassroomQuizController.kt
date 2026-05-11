package com.xapps.classroom.api.controller

import com.xapps.classroom.api.dto.SessionPreviewDTO
import com.xapps.classroom.api.dto.EvaluateClassroomQuizAttemptRequest
import com.xapps.classroom.application.participant.ParticipantClassroomQuizOrchestrator
import com.xapps.classroom.application.participant.ParticipantClassroomQuizQueryService
import com.xapps.classroom.application.participant.QuizAttemptSubmissionOrchestrator
import com.xapps.classroom.domain.model.participant.ParticipantClassroomQuiz
import com.xapps.classroom.domain.model.tutor.TutorClassroomQuiz
import com.xapps.dto.IdHolder
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/classroom/participant")
class ParticipantClassroomQuizController(
    private val participantClassroomQuizOrchestrator: ParticipantClassroomQuizOrchestrator,
    private val attemptSubmissionOrchestrator: QuizAttemptSubmissionOrchestrator,
    private val query: ParticipantClassroomQuizQueryService
) : ReactiveBaseController() {

    @PostMapping(
        "/session/preview",
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    suspend fun previewSession(
        @RequestBody joinCode: IdHolder
    ): SessionPreviewDTO =
        handle("previewSession") {
            participantClassroomQuizOrchestrator.previewSession(joinCode.id)
        }

    @PostMapping(
        "/session/enroll",
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    suspend fun enroll(
        @RequestBody joinCode: IdHolder
    ): ParticipantClassroomQuiz =
        handle("enrollParticipant") {
            val principal = getAuthenticatedUserPrincipal()

            participantClassroomQuizOrchestrator.enrollParticipant(
                userId = principal.userId,
                email = principal.email,
                joinCode = joinCode.id
            )
        }

    @PostMapping(
        "/session/attempt/evaluate",
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    suspend fun evaluate(
        @RequestBody submissionDto: EvaluateClassroomQuizAttemptRequest
    ): ParticipantClassroomQuiz =
        handle("evaluateAttempt") {
            val principal = getAuthenticatedUserPrincipal()

            attemptSubmissionOrchestrator.evaluateAttempt(
                userId = principal.userId,
                sessionId = submissionDto.sessionId,
                quizId = submissionDto.quizId,
                attempt = submissionDto.attempt
            )
        }


    @PostMapping(
        "/quiz/get",
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    suspend fun fetchParticipantQuiz(
        @RequestBody quizId: IdHolder
    ): ParticipantClassroomQuiz =
        handle("getParticipantQuiz") {
            val principal = getAuthenticatedUserPrincipal()

            query.getQuiz(
                userId = principal.userId,
                quizId = quizId.id
            )
        }
}