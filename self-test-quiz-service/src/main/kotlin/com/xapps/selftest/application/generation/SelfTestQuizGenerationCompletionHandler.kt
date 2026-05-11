package com.xapps.selftest.application.generation

import com.xapps.model.DeliveryStatus
import com.xapps.model.QuizId
import com.xapps.model.TaskDraftStatus
import com.xapps.questions.contracts.self_test_generation.dto.QuestionDTO
import com.xapps.selftest.application.useronline.policy.DeliverPendingSelfTestQuizPolicy
import com.xapps.selftest.domain.factory.PendingSelfTestQuizFactory
import com.xapps.selftest.domain.factory.SelfTestQuizFactory
import com.xapps.selftest.domain.repository.SelfTestQuizDraftRepository
import com.xapps.selftest.domain.repository.SelfTestQuizRepository
import com.xapps.selftest.domain.repository.PendingSelfTestQuizRepository
import com.xapps.time.clock.ClockProvider
import com.xapps.time.types.KotlinInstant
import org.springframework.stereotype.Component

@Component
class SelfTestQuizGenerationCompletionHandler(
    private val draftRepository: SelfTestQuizDraftRepository,
    private val quizRepository: SelfTestQuizRepository,
    private val pendingSelfTestQuizRepository: PendingSelfTestQuizRepository,
    private val quizFactory: SelfTestQuizFactory,
    private val pendingQuizFactory: PendingSelfTestQuizFactory,
    private val pendingQuizPolicy: DeliverPendingSelfTestQuizPolicy,
    private val clockProvider: ClockProvider
) {

    suspend fun complete(
        userId: String,
        quizId: QuizId,
        questions: List<QuestionDTO>
    ) {

        val draft = requireNotNull(
            draftRepository.findByQuizId(quizId = quizId)
        ) { "Classroom Quiz Draft not found for quizId=$quizId" }

        val now = clockProvider.now()

        val quiz = quizFactory.create(
            quizId = quizId,
            subject = draft.subject,
            title = draft.title,
            now = now,
            questions = questions
        )

        quizRepository.save(quiz)

        persistPendingQuiz(
            userId = userId,
            quizId = quizId,
            now = now
        )

        draftRepository.save(draft.copy(status = TaskDraftStatus.COMPLETED))
        // Push quiz payload (id) to the user in-case they are currently online
        pendingQuizPolicy.execute(userId)
    }

    private suspend fun persistPendingQuiz(
        userId: String,
        quizId: QuizId,
        now: KotlinInstant
    ) {
        val pending = pendingQuizFactory.create(
            userId = userId,
            quizId = quizId,
            status = DeliveryStatus.PENDING,
            createdAt = now
        )

        pendingSelfTestQuizRepository.save(pending)
    }
}