package com.xapps.selftest.persistence.repository.impl

import com.xapps.model.TaskDraftStatus
import com.xapps.model.QuizId
import com.xapps.selftest.domain.model.SelfTestQuizDraft
import com.xapps.selftest.domain.repository.SelfTestQuizDraftRepository
import com.xapps.selftest.persistence.entity.SelfTestQuizDraftDocument
import com.xapps.selftest.persistence.mapper.SelfTestQuizDraftMapper
import com.xapps.selftest.persistence.repository.SelfTestQuizDraftMongoRepository
import com.xapps.selftest.persistence.saveUpserting
import org.springframework.stereotype.Component

@Component
class SelfTestQuizDraftRepositoryImpl(
    private val repository: SelfTestQuizDraftMongoRepository,
    private val mapper: SelfTestQuizDraftMapper
) : SelfTestQuizDraftRepository {

    override suspend fun save(quizDraft: SelfTestQuizDraft) {
        repository.saveUpserting(mapper.toDocument(quizDraft))
    }

    override suspend fun findByQuizId(quizId: QuizId): SelfTestQuizDraft? {
        return repository.findByQuizId(quizId)?.let {
            mapper.toDomain(it)
        }
    }

    override suspend fun deleteAllByDraftStatus(status: TaskDraftStatus): Int {
        return repository.deleteAllByDraftStatusCode(status.code)
    }

    private companion object {

    }
}