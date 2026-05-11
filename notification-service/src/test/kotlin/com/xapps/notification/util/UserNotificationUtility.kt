package com.xapps.notification.util

import com.xapps.notification.repository.NotificationRepository
import com.xapps.notification.service.EmailService
import com.xapps.notification.service.FCMService
import com.xapps.studentapplication.entity.NotificationEntity
import com.xapps.studentapplication.model.NotificationType
import org.springframework.stereotype.Component

@Component
class UserNotificationUtility(
    private val notificationRepo: NotificationRepository,
    private val fcmService: FCMService,
    private val emailService: EmailService
) {

    fun notify(
        userId: String,
        userFcmToken: String?,
        userEmail: String,
        message: String? = null,
        type: NotificationType = NotificationType.SYSTEM
    ) {

        val finalMessage = message ?: type.defaultMessage
        val title = type.title

        // 1. Persist notification
        notificationRepo.save(
            NotificationEntity(
                userId = userId,
                title = title,
                message = finalMessage,
                type = type
            )
        )

        // 2. Send push (if token available)
        userFcmToken?.let { token ->
            runCatching { fcmService.sendPush(token, title, finalMessage) }
                .onFailure { println("FCM push failed: ${it.message}") }
        }

        // 3. Send email (if email available)
        userEmail.let{ email ->
            runCatching { emailService.sendSimpleEmail(email, title, finalMessage) }
                .onFailure { println("Email failed: ${it.message}") }
        }
    }

}