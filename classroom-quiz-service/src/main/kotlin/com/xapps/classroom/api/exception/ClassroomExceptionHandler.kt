package com.xapps.classroom.api.exception

import com.xapps.classroom.domain.exceptions.ClassroomDomainError
import com.xapps.dto.ApiErrorResponse
import com.xapps.dto.ApiErrorResponseType
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ClassroomExceptionHandler {

    private val log = LoggerFactory.getLogger(javaClass)

    @ExceptionHandler(ClassroomDomainError::class)
    fun handle(ex: ClassroomDomainError): ResponseEntity<ApiErrorResponse> {

        log.error("Handling Classroom exception: ${ex.toString()}")

        val response = when (ex) {

            is ClassroomDomainError.SessionClosed ->
                ApiErrorResponse(ApiErrorResponseType.SESSION_CLOSED, "Session is closed")

            is ClassroomDomainError.SessionNotInPreviewableState ->
                ApiErrorResponse(ApiErrorResponseType.SESSION_NOT_IN_PREVIEWABLE_STATE, "Session is not previewable")

            is ClassroomDomainError.SessionNotInSubmissionState ->
                ApiErrorResponse(ApiErrorResponseType.SESSION_NOT_IN_SUBMISSION_STATE, "Submission not allowed")

            is ClassroomDomainError.SessionNotInEnrollmentState ->
                ApiErrorResponse(ApiErrorResponseType.SESSION_NOT_IN_ENROLLMENT_STATE, "Enrollment not allowed")

            is ClassroomDomainError.SessionCapacityReached ->
                ApiErrorResponse(ApiErrorResponseType.SESSION_CAPACITY_REACHED, "Session max participation has been reached")

            is ClassroomDomainError.SessionNotFound ->
                ApiErrorResponse(ApiErrorResponseType.SESSION_NOT_FOUND, "Session not found: ${ex.sessionId}")

            is ClassroomDomainError.ClassroomQuizNotFound ->
                ApiErrorResponse(ApiErrorResponseType.CLASSROOM_QUIZ_NOT_FOUND, "Invalid classroom quiz: ${ex.quizId}")

            is ClassroomDomainError.InvalidJoinCode ->
                ApiErrorResponse(ApiErrorResponseType.INVALID_JOIN_CODE, "Invalid join code: ${ex.joinCode}")

            is ClassroomDomainError.ParticipantAlreadyEnrolled ->
                ApiErrorResponse(ApiErrorResponseType.PARTICIPANT_ALREADY_ENROLLED, "Already enrolled")

            is ClassroomDomainError.ParticipantNotFound ->
                ApiErrorResponse(ApiErrorResponseType.PARTICIPANT_NOT_FOUND, "Participant not found")

            is ClassroomDomainError.InvalidAttemptState ->
                ApiErrorResponse(ApiErrorResponseType.INVALID_ATTEMPT_STATE, "Invalid attempt state")

            is ClassroomDomainError.TutorAccessDenied ->
                ApiErrorResponse(ApiErrorResponseType.TUTOR_ACCESS_DENIED, "Unauthorized access")

            is ClassroomDomainError.ActiveSessionAlreadyExists ->
                ApiErrorResponse(ApiErrorResponseType.ACTIVE_SESSION_ALREADY_EXISTS, "Session already exists")
        }

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(response)
    }
}