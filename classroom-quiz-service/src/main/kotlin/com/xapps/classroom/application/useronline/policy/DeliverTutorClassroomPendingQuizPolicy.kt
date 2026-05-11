package com.xapps.classroom.application.useronline.policy

import com.xapps.classroom.application.port.out.PublishTutorQuizGeneratedEventPort
import com.xapps.classroom.domain.repository.TutorPendingClassroomQuizRepository
import com.xapps.model.DeliveryStatus
import kotlinx.coroutines.flow.toList
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class DeliverTutorClassroomPendingQuizPolicy(
    private val pendingRepository: TutorPendingClassroomQuizRepository,
    private val publishTutorQuizGeneratedEventPort: PublishTutorQuizGeneratedEventPort
) : UserOnlinePolicy {

    private val logger = LoggerFactory.getLogger(javaClass)

    override suspend fun execute(userId: String) {
        val quizIds = pendingRepository
            .findAllByUserIdAndStatus(userId, DeliveryStatus.PENDING)
            .toList()
            .map { it.quizId }

        if (quizIds.isEmpty()) return

        logger.info("Discovered ${quizIds.count()} pending classroom quizzes for tutor: $userId")

        publishTutorQuizGeneratedEventPort.publishTutorQuizGeneratedEvent(userId, quizIds)
    }
}