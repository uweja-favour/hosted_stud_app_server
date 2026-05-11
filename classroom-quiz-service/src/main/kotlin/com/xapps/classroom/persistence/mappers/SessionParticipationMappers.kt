package com.xapps.classroom.persistence.mappers

import com.xapps.classroom.domain.model.canonical__server_only.SessionParticipation
import com.xapps.classroom.persistence.entities.SessionParticipationDocument
import com.xapps.time.types.toKotlinInstant

fun SessionParticipation.toDocument(): SessionParticipationDocument {
    return SessionParticipationDocument(
        id = id,
        sessionId = sessionId,
        userId = userId,
        email = email,
        joinedAt = joinedAt,
        attempt = attempt?.toDocument()
    )
}

fun SessionParticipationDocument.toDomain(): SessionParticipation {
    return SessionParticipation(
        id = id,
        sessionId = sessionId,
        userId = userId,
        email = email,
        joinedAt = joinedAt,
        attempt = attempt?.toDomain()
    )
}