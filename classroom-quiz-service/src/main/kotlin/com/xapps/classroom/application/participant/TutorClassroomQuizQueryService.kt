package com.xapps.classroom.application.participant

import com.xapps.classroom.domain.exceptions.ClassroomDomainError
import com.xapps.classroom.domain.model.mappers.toParticipantModel
import com.xapps.classroom.domain.model.participant.ParticipantClassroomQuiz
import com.xapps.classroom.domain.repository.ClassroomQuizRepository
import com.xapps.model.QuizId
import org.springframework.stereotype.Component

@Component
class ParticipantClassroomQuizQueryService(
    private val repository: ClassroomQuizRepository
) {

    suspend fun getQuiz(
        userId: String,
        quizId: QuizId
    ): ParticipantClassroomQuiz {

        val quiz = repository.findById(quizId)
            ?: throw ClassroomDomainError.ClassroomQuizNotFound(quizId)

        return quiz.toParticipantModel(userId)
    }
}