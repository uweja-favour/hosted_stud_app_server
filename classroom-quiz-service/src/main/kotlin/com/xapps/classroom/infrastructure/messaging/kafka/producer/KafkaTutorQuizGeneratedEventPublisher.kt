package com.xapps.classroom.infrastructure.messaging.kafka.producer

import com.xapps.classroom.application.port.out.PublishTutorQuizGeneratedEventPort
import com.xapps.messaging.kafka.KafkaTopics
import com.xapps.messaging.kafka.events.ClassroomQuizDeliveredEvent
import com.xapps.model.QuizId
import com.xapps.platform.core.compression.ObjectCompressionService
import kotlinx.serialization.encodeToString
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

/**
 * Kafka-backed implementation responsible for publishing events indicating that
 * a tutor's requested classroom quiz has been generated and is ready for retrieval.
 *
 * <p>
 * This component acts as an outbound adapter in the messaging layer. It translates
 * a domain-level intent into a Kafka event consumed by downstream services
 * (specifically the realtime gateway).
 *
 * <p>
 * The published event contains:
 * - The tutor identifier
 * - A list of generated quiz IDs
 *
 * <p>
 * No quiz data is transmitted. Consumers are expected to use the quiz IDs
 * to fetch the full quiz content from the classroom quiz service.
 */
@Component
class KafkaTutorQuizGeneratedEventPublisher(
    private val kafkaTemplate: KafkaTemplate<String, ByteArray>,
    private val compressionService: ObjectCompressionService
) : PublishTutorQuizGeneratedEventPort {

    private val logger = LoggerFactory.getLogger(javaClass)

    /**
     * Publishes an event indicating that a classroom quiz requested by a tutor
     * has been successfully generated.
     *
     * <p>
     * This event is consumed by the realtime gateway, which will notify the tutor
     * via WebSocket that the quiz is ready.
     *
     * @param tutorId unique identifier of the tutor who requested the quiz.
     * @param generatedQuizIds identifiers of the generated quizzes.
     */
    override fun publishTutorQuizGeneratedEvent(
        tutorId: String,
        generatedQuizIds: List<QuizId>
    ) {
        logger.info(
            "Publishing ClassroomQuizDeliveredEvent for tutorId=$tutorId with quizIds=$generatedQuizIds"
        )

        val event = ClassroomQuizDeliveredEvent(
            tutorId = tutorId,
            quizIds = generatedQuizIds
        )

        val compressed: ByteArray = compressionService.compress(
            ClassroomQuizDeliveredEvent.serializer(),
            event
        )

        kafkaTemplate.send(
            KafkaTopics.Quiz.CLASSROOM_PAYLOAD,
            tutorId,
            compressed
        )
    }
}