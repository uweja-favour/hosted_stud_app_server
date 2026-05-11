package com.xapps.classroom.domain.model.canonical__server_only.factory

import com.xapps.classroom.domain.model.canonical__server_only.ClassroomQuiz
import com.xapps.classroom.domain.model.canonical__server_only.ClassroomSession
import com.xapps.model.QuizId
import com.xapps.model.question.Question
import com.xapps.platform.core.string.generateUniqueId
import com.xapps.time.types.KotlinDuration
import com.xapps.time.types.KotlinInstant

object ClassroomQuizFactory {

    fun create(
        quizId: QuizId,

        tutorId: String,
        tutorEmail: String,
        title: String,
        subject: String,
        topic: String?,
        description: String?,
        questions: List<Question>,

        createdAt: KotlinInstant,
        startTime: KotlinInstant,
        duration: KotlinDuration,
        submissionGraceDuration: KotlinDuration,
        maxParticipants: Int
    ): ClassroomQuiz {
        val session = createSession(
            quizId = quizId,
            createdAt = createdAt,
            startTime = startTime,
            duration = duration,
            submissionGraceDuration = submissionGraceDuration,
            maxParticipants = maxParticipants
        )

        return ClassroomQuiz(
            sessions = listOf(session),
            tutorId = tutorId,
            tutorEmail = tutorEmail,
            id = quizId,
            title = title,
            subject = subject,
            questions = questions,
            topic = topic,
            description = description,
            createdAt = createdAt,
            version = 1
        )
    }

    private fun createSession(
        quizId: QuizId,

        createdAt: KotlinInstant,
        startTime: KotlinInstant,
        duration: KotlinDuration,
        submissionGraceDuration: KotlinDuration,
        maxParticipants: Int
    ): ClassroomSession {
        return ClassroomSession(
            id = generateUniqueId(),
            quizId = quizId,
            joinCode = generateUniqueId(),
            participations = emptyList(),
            createdAt = createdAt,
            startTime = startTime,
            duration = duration,
            submissionGraceDuration = submissionGraceDuration,
            maxParticipants = maxParticipants
        )
    }
}