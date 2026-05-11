package com.xapps.classroom.persistence.entities

import com.xapps.persistence.attempt.QuizAttemptDocument
import com.xapps.time.types.KotlinInstant

data class SessionParticipationDocument(
    val id: String,
    val sessionId: String,
    val userId: String,
    val email: String,

    val joinedAt: KotlinInstant,

    // null = never submitted
    val attempt: QuizAttemptDocument?,
)