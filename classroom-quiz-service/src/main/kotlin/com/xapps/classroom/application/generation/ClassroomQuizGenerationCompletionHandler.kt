package com.xapps.classroom.application.generation

import com.xapps.classroom.application.useronline.policy.DeliverTutorClassroomPendingQuizPolicy
import com.xapps.classroom.domain.model.canonical__server_only.TutorPendingClassroomQuiz
import com.xapps.classroom.domain.model.canonical__server_only.factory.ClassroomQuizFactory
import com.xapps.classroom.domain.repository.ClassroomQuizDraftRepository
import com.xapps.classroom.domain.repository.ClassroomQuizRepository
import com.xapps.classroom.domain.repository.TutorPendingClassroomQuizRepository
import com.xapps.dto.mappers.toQuestion
import com.xapps.model.DeliveryStatus
import com.xapps.model.QuizId
import com.xapps.model.TaskDraftStatus
import com.xapps.platform.core.string.generateUniqueId
import com.xapps.questions.contracts.self_test_generation.dto.QuestionDTO
import com.xapps.time.clock.ClockProvider
import com.xapps.time.types.KotlinInstant
import org.springframework.stereotype.Component

@Component
class ClassroomQuizGenerationCompletionHandler(
    private val draftRepository: ClassroomQuizDraftRepository,
    private val quizRepository: ClassroomQuizRepository,
    private val pendingRepository: TutorPendingClassroomQuizRepository,
    private val pendingPolicy: DeliverTutorClassroomPendingQuizPolicy,
    private val clockProvider: ClockProvider
) {

    suspend fun complete(
        tutorId: String,
        quizId: QuizId,
        questions: List<QuestionDTO>
    ) {

        val draft = requireNotNull(
            draftRepository.findByQuizIdAndTutorId(quizId = quizId, tutorId = tutorId)
        ) { "Classroom Quiz Draft not found for quizId=$quizId tutorId=$tutorId" }

        val now = clockProvider.now()

        val quiz = ClassroomQuizFactory.create(
            quizId = quizId,
            tutorId = draft.tutorId,
            tutorEmail = draft.tutorEmail,
            title = draft.title,
            subject = draft.subject,
            topic = draft.topic,
            description = draft.description,
            questions = questions.mapIndexed { index, dTO ->
                dTO.toQuestion(quizId, index + 1)
            },
            createdAt = now,
            startTime = draft.startTime,
            duration = draft.duration,
            submissionGraceDuration = draft.submissionGraceDuration,
            maxParticipants = draft.maxParticipants
        )

        quizRepository.save(quiz)

        persistPendingQuiz(
            userId = tutorId,
            quizId = quizId,
            now = now
        )

        draftRepository.save(draft.copy(status = TaskDraftStatus.COMPLETED))

        // Push quiz payload (id) to tutor in case he is online.
        pendingPolicy.execute(tutorId)
    }

    private suspend fun persistPendingQuiz(
        userId: String,
        quizId: QuizId,
        now: KotlinInstant
    ) {
        pendingRepository.save(
            TutorPendingClassroomQuiz(
                id = generateUniqueId(),
                userId = userId,
                quizId = quizId,
                status = DeliveryStatus.PENDING,
                createdAt = now
            )
        )
    }
}