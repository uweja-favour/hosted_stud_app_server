@file: OptIn(ExperimentalTime::class)

package com.xapps.classroom.domain.model.canonical__server_only

import com.xapps.classroom.domain.exceptions.ClassroomDomainError
import com.xapps.classroom.domain.model.ClassroomSessionState
import com.xapps.model.QuizId
import com.xapps.time.types.KotlinDuration
import com.xapps.time.types.KotlinInstant
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlin.time.Duration.Companion.minutes
import kotlin.time.ExperimentalTime

@Serializable
data class ClassroomSession(
    val id: String,
    val quizId: QuizId,

    val joinCode: String,
    val participations: List<SessionParticipation>,

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
data class SessionTimeline(
    @Contextual val startTime: KotlinInstant,
    @Contextual val duration: KotlinDuration,
    @Contextual val submissionGraceDuration: KotlinDuration
) {
    @Contextual
    val endsAt: KotlinInstant
        get() = startTime + duration

    @Contextual
    val submissionDeadline: KotlinInstant
        get() = endsAt + submissionGraceDuration

    @Contextual
    val lobbyStartTime: KotlinInstant
        get() = startTime - 15.minutes

    fun resolveState(now: KotlinInstant): ClassroomSessionState {
        return when {
            now < lobbyStartTime -> ClassroomSessionState.PreLobby
            now < startTime -> ClassroomSessionState.Lobby
            now < endsAt -> ClassroomSessionState.Ongoing
            now < submissionDeadline -> ClassroomSessionState.SubmissionOpen
            else -> ClassroomSessionState.Closed
        }
    }

    fun isInLobby(now: KotlinInstant): Boolean =
        resolveState(now) is ClassroomSessionState.Lobby

    fun isOngoing(now: KotlinInstant): Boolean =
        resolveState(now) is ClassroomSessionState.Ongoing
}


fun ClassroomSession.ensurePreviewAllowed(now: KotlinInstant) {
    val state = timeline().resolveState(now)

    if (state is ClassroomSessionState.Closed)
        throw ClassroomDomainError.SessionClosed()

    if (state !is ClassroomSessionState.PreLobby &&
        state !is ClassroomSessionState.Lobby
    ) {
        throw ClassroomDomainError.SessionNotInPreviewableState()
    }
}

fun ClassroomSession.ensureEnrollmentAllowed(now: KotlinInstant) {
    val state = timeline().resolveState(now)

    if (state is ClassroomSessionState.Closed)
        throw ClassroomDomainError.SessionClosed()

    if (state !is ClassroomSessionState.Lobby && state !is ClassroomSessionState.PreLobby) {
        throw ClassroomDomainError.SessionNotInEnrollmentState()
    }
}


fun ClassroomSession.ensureSubmissionWindowOpen(now: KotlinInstant) {
    val state = timeline().resolveState(now)

    if (state !is ClassroomSessionState.SubmissionOpen &&
        state !is ClassroomSessionState.Ongoing
    ) {
        throw ClassroomDomainError.SessionNotInSubmissionState()
    }
}