package com.xapps.selftest.infrastructure.messaging.kafka.producer

import com.xapps.messaging.kafka.KafkaTopics
import com.xapps.messaging.kafka.events.QuestionsRequestedEvent
import com.xapps.model.QuizType
import com.xapps.questions.contracts.question_generation.JobId
import com.xapps.dto.CreateSelfTestQuizRequest
import com.xapps.model.QuizId
import com.xapps.platform.core.compression.ObjectCompressionService
import com.xapps.selftest.application.generation.FileProcessor
import com.xapps.selftest.application.port.out.QuestionGenerationRequestPublisher
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class KafkaQuestionGenerationRequestPublisher(
    private val kafkaTemplate: KafkaTemplate<String, ByteArray>,
    private val compressionService: ObjectCompressionService,
    private val fileProcessor: FileProcessor
) : QuestionGenerationRequestPublisher {

    override suspend fun requestQuestionGeneration(
        userId: String,
        quizId: QuizId,
        jobId: JobId,
        setup: CreateSelfTestQuizRequest
    ) {
        val fileKeys = fileProcessor.uploadFiles(setup.files)

        val event = QuestionsRequestedEvent(
            jobId = jobId,
            userId = userId,
            quizId = quizId,
            questionCount = setup.questionCount,
            allocations = setup.allocations,
            fileKeys = fileKeys,
            quizType = QuizType.SELF_TEST
        )

        val compressed: ByteArray = compressionService.compress(
            QuestionsRequestedEvent.serializer(),
            event
        )

        kafkaTemplate.send(
            KafkaTopics.Questions.REQUESTED,
            jobId.value,
            compressed
        )
    }
}