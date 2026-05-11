package com.xapps.classroom.domain.model.canonical__server_only

import com.xapps.model.TaskDraftStatus
import com.xapps.model.QuizId
import com.xapps.time.types.KotlinDuration
import com.xapps.time.types.KotlinInstant
import kotlinx.serialization.Contextual

data class ClassroomQuizDraft(
    val id: String,
    val quizId: QuizId,
    val title: String,
    val subject: String,
    val topic: String?,
    val description: String?,

    val tutorId: String,
    val tutorEmail: String,
    @Contextual val startTime: KotlinInstant,
    @Contextual val duration: KotlinDuration,
    @Contextual val submissionGraceDuration: KotlinDuration,
    val maxParticipants: Int,

    val status: TaskDraftStatus
)