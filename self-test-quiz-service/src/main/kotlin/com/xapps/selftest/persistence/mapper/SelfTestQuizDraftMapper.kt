package com.xapps.selftest.persistence.mapper

import com.xapps.model.TaskDraftStatus
import com.xapps.selftest.domain.model.SelfTestQuizDraft
import com.xapps.selftest.persistence.entity.SelfTestQuizDraftDocument
import org.springframework.stereotype.Component

@Component
class SelfTestQuizDraftMapper {

    fun toDocument(quizDraft: SelfTestQuizDraft): SelfTestQuizDraftDocument {
        return with(quizDraft) {
            SelfTestQuizDraftDocument(
                id1 = id,
                quizId = quizId,
                title = title,
                subject = subject,
                topic = topic,
                description = description,
                draftStatusCode = status.code
            )
        }
    }

    fun toDomain(quizDraftDocument: SelfTestQuizDraftDocument): SelfTestQuizDraft {
        return with(quizDraftDocument) {
            SelfTestQuizDraft(
                id = getTheId(),
                quizId = quizId,
                title = title,
                subject = subject,
                topic = topic,
                description = description,
                status = TaskDraftStatus.fromCode(draftStatusCode)
            )
        }
    }
}