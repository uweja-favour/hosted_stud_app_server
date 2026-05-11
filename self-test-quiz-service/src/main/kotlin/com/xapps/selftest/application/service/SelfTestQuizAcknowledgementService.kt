package com.xapps.selftest.application.service

import com.xapps.model.DeliveryStatus
import com.xapps.model.TaskDraftStatus
import com.xapps.model.QuizId
import com.xapps.selftest.domain.repository.PendingSelfTestQuizRepository
import com.xapps.selftest.domain.repository.SelfTestQuizDraftRepository
import org.springframework.stereotype.Component

@Component
class SelfTestQuizAcknowledgementService(
    private val pendingRepository: PendingSelfTestQuizRepository,
    private val draftRepository: SelfTestQuizDraftRepository
) {

    suspend fun acknowledgeQuiz(
        quizId: QuizId
    ) {
        val pendingQuiz = pendingRepository.findByQuizId(quizId)
            ?: return

        pendingRepository.save(pendingQuiz.copy(status = DeliveryStatus.DELIVERED))

        val draft = draftRepository.findByQuizId(quizId)
            ?: return

        draftRepository.save(draft.copy(status = TaskDraftStatus.COMPLETED))
    }
}