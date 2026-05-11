package com.xapps.selftest.application.useronline.policy

import com.xapps.model.DeliveryStatus
import com.xapps.selftest.application.port.out.PublishQuizGeneratedEventPort
import com.xapps.selftest.domain.repository.PendingSelfTestQuizRepository
import kotlinx.coroutines.flow.toList
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class DeliverPendingSelfTestQuizPolicy(
    private val pendingRepository: PendingSelfTestQuizRepository,
    private val notificationPublisher: PublishQuizGeneratedEventPort
) : UserOnlinePolicy {

    private val logger = LoggerFactory.getLogger(javaClass)

    override suspend fun execute(userId: String) {
        val quizIds = pendingRepository
            .findAllByUserIdAndStatus(userId, DeliveryStatus.PENDING)
            .toList()
            .map { it.quizId }

        if (quizIds.isEmpty()) return

        logger.info("Discovered ${quizIds.count()} pending self test quizzes for user: $userId")

        notificationPublisher.publishQuizGeneratedEvent(userId, quizIds)
    }
}