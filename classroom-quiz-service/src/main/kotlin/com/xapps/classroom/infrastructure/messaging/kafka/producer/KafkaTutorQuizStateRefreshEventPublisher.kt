package com.xapps.classroom.infrastructure.messaging.kafka.producer

import com.xapps.classroom.application.port.out.PublishTutorQuizStateRefreshEventPort
import com.xapps.messaging.kafka.KafkaTopics
import com.xapps.messaging.kafka.events.ClassroomQuizStateRefreshEvent
import com.xapps.model.QuizId
import com.xapps.platform.core.compression.ObjectCompressionService
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class KafkaTutorQuizStateRefreshEventPublisher(
    private val kafkaTemplate: KafkaTemplate<String, ByteArray>,
    private val compressionService: ObjectCompressionService
) : PublishTutorQuizStateRefreshEventPort {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun publishTutorQuizStateRefreshEvent(
        tutorId: String,
        quizIds: List<QuizId>
    ) {
        val event = ClassroomQuizStateRefreshEvent.Tutor(
            tutorId = tutorId,
            classroomQuizIds = quizIds
        )

        log.info("Publishing TutorQuizStateRefreshEvent for tutorId=$tutorId classroomQuizIds=$quizIds")

        val compressed: ByteArray = compressionService.compress(
            ClassroomQuizStateRefreshEvent.serializer(),
            event
        )

        kafkaTemplate.send(
            KafkaTopics.Quiz.CLASSROOM_STATE_CHANGED,
            tutorId,
            compressed
        )
    }
}