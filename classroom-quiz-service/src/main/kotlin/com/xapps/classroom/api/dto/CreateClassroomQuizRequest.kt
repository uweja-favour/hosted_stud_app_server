package com.xapps.classroom.api.dto

import com.xapps.dto.FileUploadDTO
import com.xapps.dto.QuestionAllocationDTO
import com.xapps.time.types.KotlinDuration
import com.xapps.time.types.KotlinInstant
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class CreateClassroomQuizRequest(
    val title: String,
    val subject: String,
    val topic: String?,
    val description: String?,

    val questionCount: Int,
    val questionAllocations: List<QuestionAllocationDTO>,
    val files: List<FileUploadDTO>,

    @Contextual val startTime: KotlinInstant,
    @Contextual val duration: KotlinDuration,
    @Contextual val submissionGraceDuration: KotlinDuration,

    val maxParticipants: Int
) {
    @Contextual
    val submissionDeadline: KotlinInstant
        get() = startTime.plus(duration)
}