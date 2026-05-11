package com.xapps.classroom.persistence.repositories

import com.xapps.classroom.persistence.entities.ClassroomQuizDraftDocument
import com.xapps.model.TaskDraftStatusCode
import com.xapps.model.QuizId
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ClassroomQuizDraftMongoRepository : CoroutineCrudRepository<ClassroomQuizDraftDocument, String> {

    suspend fun findByQuizIdAndTutorId(
        quizId: QuizId,
        tutorId: String
    ): ClassroomQuizDraftDocument?

    suspend fun deleteAllByDraftStatusCode(
        draftStatusCode: TaskDraftStatusCode
    ): Int
}