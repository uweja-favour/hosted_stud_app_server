package com.xapps.classroom.persistence.entities

import com.xapps.classroom.persistence.BasePersistableEntity
import com.xapps.model.DeliveryStatusCode
import com.xapps.time.types.KotlinInstant
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("tutor_pending_classroom_quiz")
data class TutorPendingClassroomQuizDocument(
    @Id
    val id1: String,
    val userId: String,
    val quizId: String,
    val statusCode: DeliveryStatusCode,
    val createdAt: KotlinInstant
): BasePersistableEntity() {
    override fun getTheId(): String = id1
}