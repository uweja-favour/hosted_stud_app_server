package com.xapps.classroom.domain.model.mappers

import com.xapps.classroom.domain.model.canonical__server_only.ClassroomQuiz
import com.xapps.classroom.domain.model.canonical__server_only.ClassroomSession
import com.xapps.classroom.domain.model.tutor.TutorClassroomQuiz
import com.xapps.classroom.domain.model.tutor.TutorSession
import com.xapps.model.question.*

fun ClassroomQuiz.toTutorModel(): TutorClassroomQuiz {
    return TutorClassroomQuiz(
        id = id,
        title = title,
        subject = subject,
        topic = topic,
        description = description,

        sessions = sessions.map { it.toTutorSession() },

        questions = questions,
        createdAt = createdAt
    )
}

private fun ClassroomSession.toTutorSession(): TutorSession {
    return TutorSession(
        session = toClientSession(),
        participations = participations.map { it.toClientSessionParticipation() }
    )
}