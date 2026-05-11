package com.xapps.classroom.domain.model.mappers

import com.xapps.classroom.domain.model.canonical__server_only.ClassroomSession
import com.xapps.classroom.domain.model.canonical__server_only.SessionParticipation
import com.xapps.classroom.domain.model.client_only.ClientClassroomSession
import com.xapps.classroom.domain.model.client_only.ClientSessionParticipant
import com.xapps.classroom.domain.model.client_only.ClientSessionParticipation

fun ClassroomSession.toClientSession(): ClientClassroomSession {
    return ClientClassroomSession(
        id = id,
        quizId = quizId,
        joinCode = joinCode,
        participants = participations.map { it.toClientSessionParticipant() },
        createdAt = createdAt,
        startTime = startTime,
        duration = duration,
        submissionGraceDuration = submissionGraceDuration,
        maxParticipants = maxParticipants
    )
}

fun SessionParticipation.toClientSessionParticipant(): ClientSessionParticipant {
    return ClientSessionParticipant(
        id = id,
        sessionId = sessionId,
        userId = userId,
        email = email,
        joinedAt = joinedAt
    )
}

fun SessionParticipation.toClientSessionParticipation(): ClientSessionParticipation {
    return ClientSessionParticipation(
        id = id,
        sessionId = sessionId,
        userId = userId,
        email = email,
        joinedAt = joinedAt,
        attempt = attempt
    )
}