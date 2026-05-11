package com.xapps.classroom.persistence.mappers

import com.xapps.classroom.domain.model.canonical__server_only.ClassroomSession
import com.xapps.classroom.persistence.entities.ClassroomSessionDocument
import com.xapps.time.types.toKotlinInstant
import kotlin.time.Duration.Companion.milliseconds

fun ClassroomSession.toDocument(): ClassroomSessionDocument {
    return ClassroomSessionDocument(
        id = id,
        quizId = quizId,
        joinCode = joinCode,
        participations = participations.map { it.toDocument() },
        createdAt = createdAt,
        startTime = startTime,
        duration = duration,
        submissionGraceDuration = submissionGraceDuration,
        maxParticipants = maxParticipants
    )
}

fun ClassroomSessionDocument.toDomain(): ClassroomSession {
    return ClassroomSession(
        id = id,
        quizId = quizId,
        joinCode = joinCode,
        participations = participations.map { it.toDomain() },
        createdAt = createdAt,
        startTime = startTime,
        duration = duration,
        submissionGraceDuration = submissionGraceDuration,
        maxParticipants = maxParticipants
    )
}