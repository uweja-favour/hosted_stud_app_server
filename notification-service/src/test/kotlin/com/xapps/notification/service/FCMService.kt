package com.xapps.notification.service

import com.google.cloud.Timestamp
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification
import com.xapps.studentapplication.admin.admin_notifier.Priority
import jakarta.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.io.FileInputStream
import java.time.LocalDateTime

@Service
class FCMService(
    @Value("\${firebase.config.path}") private val firebaseConfigPath: String
) {
    private val logger = LoggerFactory.getLogger(FCMService::class.java)

    companion object {
        private const val ADMIN_TOPIC = "admin-alerts"
        private const val MAX_TITLE_LENGTH = 100
        private const val MAX_BODY_LENGTH = 1000
    }

    @PostConstruct
    fun init() {
//        try {
//            if (FirebaseApp.getApps().isEmpty()) {
//                val options = FirebaseOptions.builder()
//                    .setCredentials(FileInputStream(firebaseConfigPath).use {
//                        com.google.auth.oauth2.GoogleCredentials.fromStream(it)
//                    })
//                    .build()
//
//                FirebaseApp.initializeApp(options)
//                logger.info("✅ FirebaseApp initialized")
//            }
//        } catch (e: Exception) {
//            logger.error("❌ FirebaseApp initialization failed: ${e.message}", e)
//        }
    }

    /**
     * Sends a push notification to an individual user via FCM token.
     */
    fun sendPush(fcmToken: String, title: String, body: String) {
        if (fcmToken.isBlank()) {
            logger.warn("⚠️ Attempted to send FCM push with blank token.")
            return
        }

        val notificationBuilder = Notification.builder()
        val notification = notificationBuilder
            .setBody(body.take(MAX_BODY_LENGTH))
            .setTitle(title.take(MAX_TITLE_LENGTH))
            .build()

        val message = Message.builder()
            .setToken(fcmToken)
            .setNotification(notification)
            .putData("title", title)
            .putData("body", body)
            .build()

        try {
            val response = FirebaseMessaging.getInstance().send(message)
            logger.info("📲 FCM push sent to token=$fcmToken → $response")
        } catch (e: Exception) {
            logger.error("❌ Failed to send FCM push to token=$fcmToken — ${e.message}", e)
        }
    }

    /**
     * Sends a push notification to all admins via FCM topic.
     * Admins must subscribe to the topic: `/topics/admin-alerts`
     */
    fun sendToAdminTopic(title: String, body: String, priority: Priority = Priority.NORMAL) {
        val notificationBuilder = Notification.builder()
        val notification = notificationBuilder
            .setBody(body.take(MAX_BODY_LENGTH))
            .setTitle(title.take(MAX_TITLE_LENGTH))
            .build()

        val message = Message.builder()
            .setTopic(ADMIN_TOPIC)
            .setNotification(notification)
            .putData("title", title)
            .putData("body", body)
            .putData("priority", priority.name)
            .putData("timestamp", LocalDateTime.now().toString())
            .build()

        try {
            val response = FirebaseMessaging.getInstance().send(message)
            logger.info("🚨 Admin alert sent via topic '$ADMIN_TOPIC' → $response")
        } catch (e: Exception) {
            logger.error("❌ Failed to send admin FCM alert — ${e.message}", e)
            throw e
        }
    }
}
