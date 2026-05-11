package com.xapps.selftest.persistence.entity

import com.xapps.persistence.QuestionDocument
import com.xapps.selftest.persistence.BasePersistableEntity
import com.xapps.persistence.attempt.QuizAttemptDocument
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("self_test_quiz")
data class SelfTestQuizDocument(
    @Id
    val id1: String,

    val title: String,
    val subject: String,
    val topic: String?,
    val description: String?,
    val createdAt: Long,

    val questions: List<QuestionDocument>,
    val attempts: List<QuizAttemptDocument>
) : BasePersistableEntity() {
    override fun getTheId(): String = id1
}