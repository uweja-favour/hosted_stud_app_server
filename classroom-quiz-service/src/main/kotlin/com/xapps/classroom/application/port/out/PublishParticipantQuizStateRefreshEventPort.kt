package com.xapps.classroom.application.port.out

import com.xapps.model.QuizId

interface PublishParticipantQuizStateRefreshEventPort {
    fun publishQuizRefreshParticipantEvent(
        quizId: QuizId,
        participantIds: List<String>
    )

    fun publishParticipantRefreshQuizzesEvent(
        participantId: String,
        quizIds: List<QuizId>
    )
}