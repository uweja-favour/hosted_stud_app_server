package com.xapps.classroom.persistence.entities

import com.xapps.classroom.persistence.BasePersistableEntity
import com.xapps.model.QuizId
import com.xapps.persistence.QuestionDocument
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.CompoundIndex
import org.springframework.data.mongodb.core.mapping.Document

@Document("classroom_quiz")
@CompoundIndex(name = "sessions_id_idx", def = "{'sessions.id': 1}")
data class ClassroomQuizDocument(
    val sessions: List<ClassroomSessionDocument>,

    val tutorId: String,
    val tutorEmail: String,
    @Id
    val id1: QuizId,
    val title: String,
    val subject: String,
    val questions: List<QuestionDocument>,
    val topic: String?,
    val description: String?,

    val createdAtMillis: Long,
    val version: Long
) : BasePersistableEntity() {
    override fun getTheId(): String =
        id1
}

