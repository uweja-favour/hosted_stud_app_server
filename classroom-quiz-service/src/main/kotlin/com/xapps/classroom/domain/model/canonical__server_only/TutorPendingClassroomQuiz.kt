package com.xapps.classroom.domain.model.canonical__server_only

import com.xapps.model.DeliveryStatus
import com.xapps.time.types.KotlinInstant

data class TutorPendingClassroomQuiz(
    val id: String,
    val userId: String,
    val quizId: String,
    val status: DeliveryStatus,
    val createdAt: KotlinInstant
)