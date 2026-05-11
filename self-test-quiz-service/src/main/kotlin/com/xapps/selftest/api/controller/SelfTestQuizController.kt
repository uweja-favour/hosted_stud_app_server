package com.xapps.selftest.api.controller

import com.xapps.dto.EmptyResponse
import com.xapps.dto.IdHolder
import com.xapps.dto.job.JobDTO
import com.xapps.dto.CreateSelfTestQuizRequest
import com.xapps.selftest.application.generation.SelfTestQuizCreationOrchestrator
import com.xapps.selftest.application.service.SelfTestQuizAcknowledgementService
import com.xapps.selftest.application.service.SelfTestQuizQueryService
import com.xapps.selftest.domain.model.SelfTestQuiz
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/self_test")
class SelfTestQuizController(
    private val query: SelfTestQuizQueryService,
    private val creation: SelfTestQuizCreationOrchestrator,
    private val acknowledgement: SelfTestQuizAcknowledgementService
) : ReactiveBaseController() {

    @PostMapping(
        "/quiz/create",
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    suspend fun createQuiz(
        @RequestBody selfTestSetupDto: CreateSelfTestQuizRequest
    ): JobDTO =
        handle("createQuiz") {
            val principal = getAuthenticatedUserPrincipal()

            creation.createQuiz(
                userId = principal.userId,
                setup = selfTestSetupDto
            )
        }


    @PostMapping(
        "/quiz/get",
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    suspend fun fetchQuiz(
        @RequestBody quizIdHolder: IdHolder
    ): SelfTestQuiz =
        handle("getQuiz") {
            query.getQuiz(quizIdHolder.id)
        }


    @PostMapping(
        "/quiz/ack",
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    suspend fun acknowledgePendingQuiz(
        @RequestBody quizIdHolder: IdHolder
    ): EmptyResponse =
        handle("ackQuiz") {

            acknowledgement.acknowledgeQuiz(quizIdHolder.id)

            EmptyResponse()
        }
}
