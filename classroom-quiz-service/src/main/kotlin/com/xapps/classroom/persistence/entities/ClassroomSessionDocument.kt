package com.xapps.classroom.persistence.entities

import com.xapps.model.QuizId
import com.xapps.time.types.KotlinDuration
import com.xapps.time.types.KotlinInstant

data class ClassroomSessionDocument(
    val id: String,
    val quizId: QuizId,

    val joinCode: String,
    val participations: List<SessionParticipationDocument>,

    val createdAt: KotlinInstant,
    val startTime: KotlinInstant,
    val duration: KotlinDuration,
    val submissionGraceDuration: KotlinDuration,

    val maxParticipants: Int
)