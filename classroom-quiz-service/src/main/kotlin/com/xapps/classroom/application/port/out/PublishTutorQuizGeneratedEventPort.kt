package com.xapps.classroom.application.port.out

import com.xapps.model.QuizId

interface PublishTutorQuizGeneratedEventPort  {
    fun publishTutorQuizGeneratedEvent(
        tutorId: String,
        generatedQuizIds: List<QuizId>
    )
}