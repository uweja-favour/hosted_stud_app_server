package com.xapps.studentapplication.dto

import java.time.Instant


data class NotificationDto(
    val id: Long,

    val title: String,

    val message: String,

    val timestamp: Instant = Instant.now()
)