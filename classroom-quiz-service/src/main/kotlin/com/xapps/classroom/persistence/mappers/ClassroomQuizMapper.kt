package com.xapps.classroom.persistence.mappers

import com.xapps.classroom.domain.model.canonical__server_only.ClassroomQuiz
import com.xapps.classroom.persistence.entities.ClassroomQuizDocument
import com.xapps.persistence.mapper.toDocument
import com.xapps.persistence.mapper.toDomain
import com.xapps.time.types.toKotlinInstant

fun ClassroomQuizDocument.toDomain(): ClassroomQuiz {
    return ClassroomQuiz(
        id = getTheId(),
        sessions = sessions.map { it.toDomain() },
        tutorId = tutorId,
        tutorEmail = tutorEmail,
        title = title,
        subject = subject,
        questions = questions.map { it.toDomain() },
        topic = topic,
        description = description,
        createdAt = createdAtMillis.toKotlinInstant(),
        version = version
    )
}

fun ClassroomQuiz.toDocument(): ClassroomQuizDocument {
    return ClassroomQuizDocument(
        id1 = id,
        sessions = sessions.map { it.toDocument() },
        tutorId = tutorId,
        tutorEmail = tutorEmail,
        title = title,
        subject = subject,
        questions = questions.map { it.toDocument() },
        topic = topic,
        description = description,
        createdAtMillis = createdAt.toEpochMilliseconds(),
        version = version
    )
}
