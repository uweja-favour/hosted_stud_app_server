package com.xapps.selftest.persistence.entity

import com.xapps.model.TaskDraftStatusCode
import com.xapps.model.QuizId
import com.xapps.selftest.persistence.BasePersistableEntity
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("self_test_quiz_draft")
data class SelfTestQuizDraftDocument(
    @Id
    val id1: String,

    val quizId: QuizId,
    val title: String,
    val subject: String,
    val topic: String?,
    val description: String?,

    val draftStatusCode: TaskDraftStatusCode
) : BasePersistableEntity() {

    override fun getTheId(): String = id1
}