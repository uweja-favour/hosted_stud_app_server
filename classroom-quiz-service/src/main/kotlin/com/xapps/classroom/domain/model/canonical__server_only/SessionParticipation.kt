package com.xapps.classroom.domain.model.canonical__server_only

import com.xapps.model.attempt.QuizAttempt
import com.xapps.time.types.KotlinInstant
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

// FOR SERVER USE ONLY.
@Serializable
data class SessionParticipation(
    val id: String,
    val sessionId: String,
    val userId: String,
    val email: String,

    @Contextual val joinedAt: KotlinInstant,

    // null = never submitted
    val attempt: QuizAttempt?
)