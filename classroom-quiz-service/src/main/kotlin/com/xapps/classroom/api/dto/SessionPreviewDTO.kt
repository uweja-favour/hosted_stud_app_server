package com.xapps.classroom.api.dto

import com.xapps.classroom.domain.model.ClassroomSessionState
import com.xapps.model.QuizId
import com.xapps.time.types.KotlinDuration
import com.xapps.time.types.KotlinInstant
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlin.time.Duration.Companion.minutes

@Serializable
data class SessionPreviewDTO(
    val quizId: QuizId,
    val title: String,
    val subject: String,
    val topic: String?,
    val description: String?,

    val sessionId: String,
    @Contextual val createdAt: KotlinInstant,
    @Contextual val startTime: KotlinInstant,
    @Contextual val duration: KotlinDuration,
    @Contextual val submissionGraceDuration: KotlinDuration,

    val maxParticipants: Int,
    val currentParticipants: Int
) {

    @Contextual
    val completionTime: KotlinInstant
        get() = startTime.plus(duration)

    @Contextual
    val submissionDeadline: KotlinInstant
        get() = completionTime.plus(submissionGraceDuration)

    @Contextual
    val lobbyStartTime: KotlinInstant
        get() = startTime.minus(5.minutes)

    fun resolveState(now: KotlinInstant): ClassroomSessionState {
        return when {
            now < lobbyStartTime -> ClassroomSessionState.PreLobby
            now < startTime -> ClassroomSessionState.Lobby
            now < completionTime -> ClassroomSessionState.Ongoing
            now < submissionDeadline -> ClassroomSessionState.SubmissionOpen
            else -> ClassroomSessionState.Closed
        }
    }
}