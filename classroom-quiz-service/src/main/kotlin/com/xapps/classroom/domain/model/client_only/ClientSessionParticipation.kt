package com.xapps.classroom.domain.model.client_only

import com.xapps.model.attempt.QuizAttempt
import com.xapps.time.types.KotlinInstant
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

// FOR CLIENT USE ONLY.

@Serializable
data class ClientSessionParticipation(
    val id: String,
    val sessionId: String,
    val userId: String,
    val email: String,

    @Contextual val joinedAt: KotlinInstant,

    // null = joined but no submission
    val attempt: QuizAttempt?
)