package com.xapps.notification.service

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service

@Service
class EmailService(
    @Autowired private val mailSender: JavaMailSender
) {
    private val logger = LoggerFactory.getLogger(EmailService::class.java)

    fun sendSimpleEmail(to: String, subject: String, content: String) {
        try {
            val message = SimpleMailMessage()
            message.setTo(to)
            message.subject = subject
            message.text = content
            mailSender.send(message)
            logger.info("📧 Email sent to $to")
        } catch (e: Exception) {
            logger.error("❌ Email sending failed to $to: ${e.message}")
        }
    }
}
