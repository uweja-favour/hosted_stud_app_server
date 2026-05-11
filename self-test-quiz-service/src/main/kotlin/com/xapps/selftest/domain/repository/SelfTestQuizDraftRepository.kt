package com.xapps.selftest.domain.repository

import com.xapps.model.TaskDraftStatus
import com.xapps.model.QuizId
import com.xapps.selftest.domain.model.SelfTestQuizDraft

interface SelfTestQuizDraftRepository {

    suspend fun save(quizDraft: SelfTestQuizDraft)

    suspend fun findByQuizId(quizId: QuizId): SelfTestQuizDraft?

    suspend fun deleteAllByDraftStatus(
        status: TaskDraftStatus
    ): Int
}