package com.xapps.studentapplication.entity

import com.xapps.studentapplication.dto.NotificationDto
import com.xapps.studentapplication.model.NotificationType
import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "notifications")
data class NotificationEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,  // <-- nullable with default

    @Column(nullable = false)
    val userId: String,

    @Column(nullable = false)
    val title: String,

    @Column(nullable = false)
    val message: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val type: NotificationType = NotificationType.SYSTEM,

    @Column(nullable = false)
    val timestamp: Instant = Instant.now(),

    @Column(name = "is_read", nullable = false)
    val read: Boolean = false
)


fun List<NotificationEntity>.toNotificationDto(): List<NotificationDto> {
    return this.map { it.toNotificationDto() }
}

fun NotificationEntity.toNotificationDto(): NotificationDto {
    return NotificationDto(
        id = id!!,
        title = title,
        message = message,
        timestamp = timestamp
    )
}