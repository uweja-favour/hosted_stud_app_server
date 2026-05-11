package com.xapps.classroom.application.tutor

import com.xapps.classroom.domain.exceptions.ClassroomDomainError
import com.xapps.classroom.domain.model.mappers.toTutorModel
import com.xapps.classroom.domain.model.tutor.TutorClassroomQuiz
import com.xapps.classroom.domain.repository.ClassroomQuizRepository
import com.xapps.model.QuizId
import org.springframework.stereotype.Component

@Component
class TutorClassroomQuizQueryService(
    private val repository: ClassroomQuizRepository
) {

    suspend fun getQuiz(
        tutorId: String,
        quizId: QuizId
    ): TutorClassroomQuiz {

        val quiz = repository.findById(quizId)
            ?: throw ClassroomDomainError.ClassroomQuizNotFound(quizId)

        if (quiz.tutorId != tutorId)
            throw ClassroomDomainError.TutorAccessDenied(
                quizId = quizId, tutorId = tutorId
            )

        return quiz.toTutorModel()
    }
}