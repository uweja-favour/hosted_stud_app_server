package com.xapps.classroom.domain.model.client_only

import com.xapps.classroom.domain.model.canonical__server_only.SessionTimeline
import com.xapps.model.QuizId
import com.xapps.time.types.KotlinDuration
import com.xapps.time.types.KotlinInstant
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class ClientClassroomSession(
    val id: String,
    val quizId: QuizId,

    val joinCode: String,
    val participants: List<ClientSessionParticipant>,

    @Contextual val createdAt: KotlinInstant,
    @Contextual val startTime: KotlinInstant,
    @Contextual val duration: KotlinDuration,
    @Contextual val submissionGraceDuration: KotlinDuration,

    val maxParticipants: Int
) {
    fun timeline(): SessionTimeline = SessionTimeline(
        startTime = startTime,
        duration = duration,
        submissionGraceDuration = submissionGraceDuration
    )
}


@Serializable
data class ClientSessionParticipant(
    val id: String,
    val sessionId: String,
    val userId: String,
    val email: String,

    @Contextual val joinedAt: KotlinInstant
)