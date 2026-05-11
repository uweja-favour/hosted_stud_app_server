package com.xapps.classroom.domain.model.mappers

import com.xapps.classroom.domain.model.canonical__server_only.ClassroomQuiz
import com.xapps.classroom.domain.model.canonical__server_only.ClassroomSession
import com.xapps.classroom.domain.model.participant.ParticipantClassroomQuiz
import com.xapps.classroom.domain.model.participant.ParticipantSession
import com.xapps.classroom.domain.model.participant.question.*
import com.xapps.model.question.FibQuestion
import com.xapps.model.question.McQuestion
import com.xapps.model.question.MsQuestion
import com.xapps.model.question.Question
import com.xapps.model.question.TfQuestion

fun ClassroomQuiz.toParticipantModel(userId: String): ParticipantClassroomQuiz {
    return ParticipantClassroomQuiz(
        id = id,
        title = title,
        subject = subject,
        topic = topic,
        description = description,
        tutorEmail = tutorEmail,

        sessions = sessions.map { it.toParticipantSession(userId) },

        questions = questions.map { it.toParticipantQuestion() }
    )
}

private fun ClassroomSession.toParticipantSession(
    userId: String
): ParticipantSession {
    val participation = participations
        .firstOrNull { it.userId == userId }

    return ParticipantSession(
        session = toClientSession(),
        participation = participation?.toClientSessionParticipation()
    )
}

private fun Question.toParticipantQuestion(): ParticipantQuestion =
    when (this) {
        is McQuestion -> ParticipantMcQuestion(
            id = id,
            text = text,
            number = number,
            difficulty = difficulty,
            topic = topic,
            options = options
        )

        is MsQuestion -> ParticipantMsQuestion(
            id = id,
            text = text,
            number = number,
            difficulty = difficulty,
            topic = topic,
            options = options
        )

        is TfQuestion -> ParticipantTfQuestion(
            id = id,
            text = text,
            number = number,
            difficulty = difficulty,
            topic = topic,
            options = options
        )

        is FibQuestion -> ParticipantFibQuestion(
            id = id,
            text = text,
            number = number,
            difficulty = difficulty,
            topic = topic
        )
    }