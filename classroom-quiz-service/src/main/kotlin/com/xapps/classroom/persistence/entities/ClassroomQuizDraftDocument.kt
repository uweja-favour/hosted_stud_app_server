package com.xapps.classroom.persistence.entities

import com.xapps.classroom.persistence.BasePersistableEntity
import com.xapps.model.TaskDraftStatusCode
import com.xapps.model.QuizId
import com.xapps.time.types.KotlinDuration
import com.xapps.time.types.KotlinInstant
import kotlinx.serialization.Contextual
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("classroom_quiz_draft")
data class ClassroomQuizDraftDocument(
    @Id
    val id1: String, // This is not the quiz id.

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

    val draftStatusCode: TaskDraftStatusCode
) : BasePersistableEntity() {
    override fun getTheId(): String = id1
}

