package com.xapps.classroom.api.controller

import com.xapps.classroom.application.tutor.TutorClassroomQuizAcknowledgementService
import com.xapps.classroom.application.tutor.TutorClassroomQuizQueryService
import com.xapps.classroom.application.tutor.TutorQuizCreationOrchestrator
import com.xapps.classroom.domain.model.tutor.TutorClassroomQuiz
import com.xapps.classroom.api.dto.CreateClassroomQuizRequest
import com.xapps.classroom.api.dto.CreateClassroomQuizSessionRequest
import com.xapps.classroom.application.tutor.TutorQuizSessionService
import com.xapps.dto.EmptyResponse
import com.xapps.dto.IdHolder
import com.xapps.dto.job.JobDTO
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

// Controllers should be named: [Actor] + [Domain Scope] + Controller
@RestController
@RequestMapping("/api/classroom/tutor")
class TutorClassroomQuizController(
    private val creation: TutorQuizCreationOrchestrator,
    private val sessionCreation: TutorQuizSessionService,
    private val query: TutorClassroomQuizQueryService,
    private val acknowledgment: TutorClassroomQuizAcknowledgementService,
) : ReactiveBaseController() {

    @PostMapping(
        "/quiz/create",
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    suspend fun createQuiz(
        @RequestBody request: CreateClassroomQuizRequest
    ): JobDTO =
        handle("createClassroomQuiz") {
            val principal = getAuthenticatedUserPrincipal()

            creation.createQuiz(
                tutorId = principal.userId,
                tutorEmail = principal.email,
                request = request
            )
        }


    @PostMapping(
        "/quiz/session/create",
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    suspend fun createQuizSession(
        @RequestBody setup: CreateClassroomQuizSessionRequest
    ): EmptyResponse =
        handle("createSession") {

            sessionCreation.createSession(request = setup)

            EmptyResponse()
        }


    @PostMapping(
        "/quiz/get",
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    suspend fun fetchTutorQuiz(
        @RequestBody quizId: IdHolder
    ): TutorClassroomQuiz =
        handle("getTutorQuiz") {
            val principal = getAuthenticatedUserPrincipal()

            query.getQuiz(
                tutorId = principal.userId,
                quizId = quizId.id
            )
        }


    @PostMapping(
        "/quiz/ack",
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    suspend fun ackTutorQuiz(
        @RequestBody quizId: IdHolder
    ): EmptyResponse =
        handle("ackQuiz") {
            val principal = getAuthenticatedUserPrincipal()

            acknowledgment.acknowledgeQuiz(
                tutorId = principal.userId,
                quizId = quizId.id
            )

            EmptyResponse()
        }
}