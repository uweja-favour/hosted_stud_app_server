package com.xapps.classroom.api.dto

import com.xapps.dto.QuestionAllocationDTO
import com.xapps.model.QuizId
import com.xapps.time.types.KotlinDuration
import com.xapps.time.types.KotlinInstant
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class CreateClassroomQuizSessionRequest(
    val quizId: QuizId,

    @Contextual val startTime: KotlinInstant,
    @Contextual val duration: KotlinDuration,
    @Contextual val submissionGraceDuration: KotlinDuration,

    val maxParticipants: Int
) {

    @Contextual
    val submissionDeadline: KotlinInstant
        get() = startTime.plus(duration)
}