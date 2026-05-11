package com.xapps.classroom.application.tutor

import com.xapps.classroom.application.port.out.QuestionGenerationRequestPublisher
import com.xapps.classroom.domain.model.canonical__server_only.ClassroomQuizDraft
import com.xapps.classroom.domain.repository.ClassroomQuizDraftRepository
import com.xapps.classroom.api.dto.CreateClassroomQuizRequest
import com.xapps.dto.job.JobDTO
import com.xapps.dto.job.JobStatus
import com.xapps.dto.job.JobTask
import com.xapps.model.TaskDraftStatus
import com.xapps.platform.core.string.generateUniqueId
import com.xapps.questions.contracts.question_generation.JobId
import org.springframework.stereotype.Component

@Component
class TutorQuizCreationOrchestrator(
    private val draftRepository: ClassroomQuizDraftRepository,
    private val questionPublisher: QuestionGenerationRequestPublisher
) {

    suspend fun createQuiz(
        tutorId: String,
        tutorEmail: String,
        request: CreateClassroomQuizRequest
    ): JobDTO {

        val quizId = generateUniqueId()
        val jobId = JobId.of(generateUniqueId())

        val draft = ClassroomQuizDraft(
            id = generateUniqueId(),
            quizId = quizId,
            title = request.title,
            subject = request.subject,
            topic = request.topic,
            description = request.description,
            tutorId = tutorId,
            tutorEmail = tutorEmail,
            startTime = request.startTime,
            duration = request.duration,
            submissionGraceDuration = request.submissionGraceDuration,
            maxParticipants = request.maxParticipants,
            status = TaskDraftStatus.IN_PROGRESS
        )

        draftRepository.save(draft)

        questionPublisher.requestQuestionGeneration(
            tutorId = tutorId,
            quizId = quizId,
            jobId = jobId,
            setup = request
        )

        return JobDTO(
            jobId = jobId,
            task = JobTask.CLASSROOM,
            status = JobStatus.Queued
        )
    }
}