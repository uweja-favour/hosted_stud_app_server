package com.xapps.selftest.domain.model

import com.xapps.model.TaskDraftStatus
import com.xapps.model.QuizId

data class SelfTestQuizDraft(
    val id: String,

    val quizId: QuizId,
    val title: String,
    val subject: String,
    val topic: String?,
    val description: String?,

    val status: TaskDraftStatus
)