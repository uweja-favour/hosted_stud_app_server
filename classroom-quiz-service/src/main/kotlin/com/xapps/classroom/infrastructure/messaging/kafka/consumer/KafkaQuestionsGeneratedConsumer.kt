package com.xapps.classroom.infrastructure.messaging.kafka.consumer

import com.xapps.classroom.application.generation.ClassroomQuizGenerationCompletionHandler
import com.xapps.messaging.kafka.KafkaGroupIds
import com.xapps.messaging.kafka.KafkaTopics
import com.xapps.messaging.kafka.events.QuestionsGeneratedEvent
import com.xapps.platform.core.compression.ObjectCompressionService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class KafkaQuestionsGeneratedConsumer(
    private val quizGenerationCompletionHandler: ClassroomQuizGenerationCompletionHandler,
    private val compressionService: ObjectCompressionService
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @KafkaListener(
        topics = [KafkaTopics.Questions.CLASSROOM_GENERATED],
        groupId = KafkaGroupIds.CLASSROOM_SERVICE_GROUP
    )
    fun handle(payload: ByteArray) {

        log.info("QuestionsGeneratedEvent CONSUMED")

        val event = compressionService.decompress(
            QuestionsGeneratedEvent.serializer(),
            payload
        )

        CoroutineScope(Dispatchers.Default).launch {
            quizGenerationCompletionHandler.complete(
                tutorId = event.userId,
                quizId = event.quizId,
                questions = event.questions
            )
        }
    }
}