package com.xapps.classroom.application.tutor

import com.xapps.classroom.domain.exceptions.ClassroomDomainError
import com.xapps.classroom.domain.repository.ClassroomQuizDraftRepository
import com.xapps.classroom.domain.repository.TutorPendingClassroomQuizRepository
import com.xapps.model.DeliveryStatus
import com.xapps.model.TaskDraftStatus
import com.xapps.model.QuizId
import org.springframework.stereotype.Component

@Component
class TutorClassroomQuizAcknowledgementService(
    private val pendingRepository: TutorPendingClassroomQuizRepository,
    private val draftRepository: ClassroomQuizDraftRepository
) {

    suspend fun acknowledgeQuiz(
        tutorId: String,
        quizId: QuizId
    ) {
        val pendingQuiz = pendingRepository.findByQuizId(quizId)
            ?: return

        if (pendingQuiz.userId != tutorId) {
            throw ClassroomDomainError.TutorAccessDenied(
                quizId = quizId,
                tutorId = tutorId
            )
        }

        pendingRepository.save(pendingQuiz.copy(status = DeliveryStatus.DELIVERED))

        val draft = draftRepository.findByQuizIdAndTutorId(quizId = quizId, tutorId = tutorId)
            ?: return

        draftRepository.save(draft.copy(status = TaskDraftStatus.COMPLETED))
    }
}