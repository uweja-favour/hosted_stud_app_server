package com.xapps.classroom.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class ClassroomSessionState {

    @Serializable
    @SerialName("PreLobby")
    data object PreLobby : ClassroomSessionState()

    @Serializable
    @SerialName("Lobby")
    data object Lobby : ClassroomSessionState() // Waiting for startTime

    @Serializable
    @SerialName("Ongoing")
    data object Ongoing : ClassroomSessionState()  // Actively answering

    @Serializable
    @SerialName("SubmissionOpen")
    data object SubmissionOpen : ClassroomSessionState() // Grace window for offline submissions and Completion duration reached (locked)

    @Serializable
    @SerialName("Closed")
    data object Closed : ClassroomSessionState() // Hard cutoff, no submissions accepted
}