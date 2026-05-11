package com.xapps.classroom.domain.model.canonical__server_only.factory

import com.xapps.classroom.domain.model.canonical__server_only.ClassroomSession
import com.xapps.model.QuizId
import com.xapps.platform.core.string.generateUniqueId
import com.xapps.time.types.KotlinDuration
import com.xapps.time.types.KotlinInstant
import kotlinx.serialization.Contextual

object ClassroomSessionFactory {

    fun create(
        quizId: QuizId,
        now: KotlinInstant,

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
            createdAt = now,
            startTime = startTime,
            duration = duration,
            submissionGraceDuration = submissionGraceDuration,
            maxParticipants = maxParticipants
        )
    }
}