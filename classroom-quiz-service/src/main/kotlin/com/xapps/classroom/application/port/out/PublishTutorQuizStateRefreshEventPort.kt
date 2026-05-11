package com.xapps.classroom.application.port.out

import com.xapps.model.QuizId

interface PublishTutorQuizStateRefreshEventPort {
    fun publishTutorQuizStateRefreshEvent(
        tutorId: String,
        quizIds: List<QuizId>
    )
}

