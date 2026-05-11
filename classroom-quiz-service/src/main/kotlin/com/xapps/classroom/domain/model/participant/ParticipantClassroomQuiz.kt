package com.xapps.classroom.domain.model.participant

import com.xapps.classroom.domain.model.participant.question.ParticipantQuestion
import com.xapps.classroom.domain.model.client_only.ClientClassroomSession
import com.xapps.classroom.domain.model.client_only.ClientSessionParticipation
import com.xapps.model.QuizId
import kotlinx.serialization.Serializable

@Serializable
data class ParticipantClassroomQuiz(
    val id: QuizId,
    val title: String,
    val subject: String,
    val topic: String?,
    val description: String?,

    val tutorEmail: String,

    val sessions: List<ParticipantSession>,
    val questions: List<ParticipantQuestion>
)

@Serializable
data class ParticipantSession(
    val session: ClientClassroomSession,

    // null → user has no participation record for this session
    val participation: ClientSessionParticipation?
)
