package com.xapps.selftest.domain.model

import com.xapps.model.DeliveryStatus
import com.xapps.time.types.KotlinInstant

data class PendingSelfTestQuiz(
    val id: String,
    val userId: String,
    val quizId: String,
    val status: DeliveryStatus,
    val createdAt: KotlinInstant
)