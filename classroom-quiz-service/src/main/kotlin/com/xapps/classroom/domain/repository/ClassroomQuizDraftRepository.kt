package com.xapps.classroom.domain.repository

import com.xapps.classroom.domain.model.canonical__server_only.ClassroomQuizDraft
import com.xapps.model.TaskDraftStatus
import com.xapps.model.QuizId

interface ClassroomQuizDraftRepository {

    suspend fun save(quizDraft: ClassroomQuizDraft)

    suspend fun findByQuizIdAndTutorId(
        quizId: QuizId,
        tutorId: String
    ): ClassroomQuizDraft?

    suspend fun deleteAllByQuizDraftStatus(
        quizDraftStatus: TaskDraftStatus
    ): Int
}