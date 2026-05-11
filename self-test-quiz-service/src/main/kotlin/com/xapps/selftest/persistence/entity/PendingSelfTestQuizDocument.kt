package com.xapps.selftest.persistence.entity

import com.xapps.model.DeliveryStatusCode
import com.xapps.selftest.persistence.BasePersistableEntity
import com.xapps.time.types.KotlinInstant
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("user_pending_self_test_quiz")
data class PendingSelfTestQuizDocument(
    @Id
    val id1: String,
    val userId: String,
    val quizId: String,
    val statusCode: DeliveryStatusCode,
    val createdAt: KotlinInstant
): BasePersistableEntity() {
    override fun getTheId(): String = id1
}