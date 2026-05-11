package com.xapps.selftest.infrastructure.messaging.kafka.producer

import com.xapps.messaging.kafka.KafkaTopics
import com.xapps.messaging.kafka.events.SelfTestQuizDeliveredEvent
import com.xapps.model.QuizId
import com.xapps.platform.core.compression.ObjectCompressionService
import com.xapps.selftest.application.port.out.PublishQuizGeneratedEventPort
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class KafkaSelfTestQuizGeneratedEventPublisher(
    private val kafkaTemplate: KafkaTemplate<String, ByteArray>,
    private val compressionService: ObjectCompressionService
) : PublishQuizGeneratedEventPort {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun publishQuizGeneratedEvent(
        userId: String,
        generatedQuizIds: List<QuizId>
    ) {
        log.info("Sending new Self Test Quizzes to user. QuizIds: $generatedQuizIds")

        val event = SelfTestQuizDeliveredEvent(
            userId = userId,
            quizIds = generatedQuizIds
        )

        val compressed: ByteArray = compressionService.compress(
            SelfTestQuizDeliveredEvent.serializer(),
            event
        )

        kafkaTemplate.send(
            KafkaTopics.Quiz.SELF_TEST_PAYLOAD,
            userId,
            compressed
        )
    }
}