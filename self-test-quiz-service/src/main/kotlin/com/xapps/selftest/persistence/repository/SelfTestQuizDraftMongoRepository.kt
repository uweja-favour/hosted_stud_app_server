package com.xapps.selftest.persistence.repository

import com.xapps.model.TaskDraftStatusCode
import com.xapps.model.QuizId
import com.xapps.selftest.persistence.entity.SelfTestQuizDraftDocument
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface SelfTestQuizDraftMongoRepository : CoroutineCrudRepository<SelfTestQuizDraftDocument, String> {

    suspend fun findByQuizId(quizId: QuizId): SelfTestQuizDraftDocument?

    suspend fun deleteAllByDraftStatusCode(
        draftStatusCode: TaskDraftStatusCode
    ): Int
}