package com.xapps.classroom.infrastructure.messaging.kafka.producer

import com.xapps.classroom.api.dto.CreateClassroomQuizRequest
import com.xapps.classroom.application.generation.FileProcessor
import com.xapps.classroom.application.port.out.QuestionGenerationRequestPublisher
import com.xapps.messaging.kafka.KafkaTopics
import com.xapps.messaging.kafka.events.QuestionsRequestedEvent
import com.xapps.model.QuizType
import com.xapps.platform.core.compression.ObjectCompressionService
import com.xapps.questions.contracts.question_generation.JobId
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class KafkaQuestionGenerationRequestPublisher(
    private val kafkaTemplate: KafkaTemplate<String, ByteArray>,
    private val compressionService: ObjectCompressionService,
    private val fileProcessor: FileProcessor
) : QuestionGenerationRequestPublisher {

    override suspend fun requestQuestionGeneration(
        tutorId: String,
        quizId: String,
        jobId: JobId,
        setup: CreateClassroomQuizRequest
    ) {
        val fileKeys = fileProcessor.uploadFiles(setup.files)

        val event = QuestionsRequestedEvent(
            jobId = jobId,
            quizId = quizId,
            userId = tutorId,
            questionCount = setup.questionCount,
            allocations = setup.questionAllocations,
            fileKeys = fileKeys,
            quizType = QuizType.CLASSROOM
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