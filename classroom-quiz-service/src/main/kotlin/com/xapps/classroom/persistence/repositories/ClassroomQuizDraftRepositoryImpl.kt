package com.xapps.classroom.persistence.repositories

import com.xapps.classroom.domain.model.canonical__server_only.ClassroomQuizDraft
import com.xapps.classroom.domain.repository.ClassroomQuizDraftRepository
import com.xapps.classroom.persistence.entities.ClassroomQuizDraftDocument
import com.xapps.classroom.persistence.saveUpserting
import com.xapps.model.TaskDraftStatus
import com.xapps.model.QuizId
import org.springframework.stereotype.Component

@Component
class ClassroomQuizDraftRepositoryImpl(
    private val repository: ClassroomQuizDraftMongoRepository,
) : ClassroomQuizDraftRepository {

    override suspend fun save(quizDraft: ClassroomQuizDraft) =
        repository.saveUpserting(toEntity(quizDraft))

    override suspend fun findByQuizIdAndTutorId(
        quizId: QuizId,
        tutorId: String
    ): ClassroomQuizDraft? {
        val quizDraftDocument = repository.findByQuizIdAndTutorId(quizId = quizId, tutorId = tutorId)
            ?: return null

        return toDomain(quizDraftDocument)
    }

    override suspend fun deleteAllByQuizDraftStatus(quizDraftStatus: TaskDraftStatus): Int {
        return repository.deleteAllByDraftStatusCode(quizDraftStatus.code)
    }

    private companion object {
        fun toEntity(quizDraft: ClassroomQuizDraft): ClassroomQuizDraftDocument {
            return with(quizDraft) {
                ClassroomQuizDraftDocument(
                    id1 = id,
                    quizId = quizId,
                    title = title,
                    subject = subject,
                    topic = topic,
                    description = description,
                    tutorId = tutorId,
                    tutorEmail = tutorEmail,

                    startTime = startTime,
                    duration = duration,
                    submissionGraceDuration = submissionGraceDuration,
                    maxParticipants = maxParticipants,

                    draftStatusCode = status.code
                )
            }
        }

        fun toDomain(quizDraftDocument: ClassroomQuizDraftDocument): ClassroomQuizDraft {
            return with(quizDraftDocument) {
                ClassroomQuizDraft(
                    id = getTheId(),
                    quizId = quizId,
                    title = title,
                    subject = subject,
                    topic = topic,
                    description = description,
                    tutorId = tutorId,
                    tutorEmail = tutorEmail,

                    startTime = startTime,
                    duration = duration,
                    submissionGraceDuration = submissionGraceDuration,
                    maxParticipants = maxParticipants,

                    status = TaskDraftStatus.fromCode(draftStatusCode)
                )
            }
        }
    }
}