package com.xapps.classroom.infrastructure.messaging.kafka.producer

import com.xapps.classroom.application.port.out.PublishParticipantQuizStateRefreshEventPort
import com.xapps.messaging.kafka.KafkaTopics
import com.xapps.messaging.kafka.events.ClassroomQuizStateRefreshEvent
import com.xapps.model.QuizId
import com.xapps.platform.core.compression.ObjectCompressionService
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class KafkaParticipantQuizStateRefreshEventPublisher(
    private val kafkaTemplate: KafkaTemplate<String, ByteArray>,
    private val compressionService: ObjectCompressionService
) : PublishParticipantQuizStateRefreshEventPort {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun publishQuizRefreshParticipantEvent(
        quizId: QuizId,
        participantIds: List<String>
    ) {
        val event = ClassroomQuizStateRefreshEvent.QuizRefreshParticipants(
            classroomQuizId = quizId,
            participantIds = participantIds,
        )

        log.info("Publishing ClassroomQuizStateRefreshEvent.QuizRefreshParticipants for classroomQuizId=$quizId participantIds=$participantIds")

        val compressed: ByteArray = compressionService.compress(
            ClassroomQuizStateRefreshEvent.serializer(),
            event
        )

        kafkaTemplate.send(
            KafkaTopics.Quiz.CLASSROOM_STATE_CHANGED,
            quizId,
            compressed
        )
    }

    override fun publishParticipantRefreshQuizzesEvent(
        participantId: String,
        quizIds: List<QuizId>
    ) {
        val event = ClassroomQuizStateRefreshEvent.ParticipantRefreshQuizzes(
            participantId = participantId,
            classroomQuizIds = quizIds
        )

        log.info("Publishing ClassroomQuizStateRefreshEvent.ParticipantRefreshQuizzes for classroomQuizId=$quizIds")

        val compressed: ByteArray = compressionService.compress(
            ClassroomQuizStateRefreshEvent.serializer(),
            event
        )

        kafkaTemplate.send(
            KafkaTopics.Quiz.CLASSROOM_STATE_CHANGED,
            participantId,
            compressed
        )
    }
}