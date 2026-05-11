package com.xapps.classroom.domain.exceptions

import com.xapps.model.QuizId

sealed class ClassroomDomainError : RuntimeException() {


    // ─── SESSION STATE VIOLATIONS ─────────────────────────────

    class SessionClosed : ClassroomDomainError()

    class SessionNotInPreviewableState : ClassroomDomainError()

    class SessionNotInEnrollmentState : ClassroomDomainError()

    class SessionNotInSubmissionState : ClassroomDomainError()


    // ─── SESSION CONSTRAINTS ────────────────────────────────

    class SessionCapacityReached : ClassroomDomainError()

    data class SessionNotFound(val sessionId: String) : ClassroomDomainError()

    data class ActiveSessionAlreadyExists(val quizId: QuizId) : ClassroomDomainError()


    // ─── QUIZ ERRORS ────────────────────────────────────────

    data class ClassroomQuizNotFound(val quizId: QuizId) : ClassroomDomainError()


    // ─── JOIN / ENROLLMENT ──────────────────────────────────

    data class InvalidJoinCode(val joinCode: String) : ClassroomDomainError()

    class ParticipantAlreadyEnrolled : ClassroomDomainError()

    class ParticipantNotFound : ClassroomDomainError()


    // ─── ATTEMPT ERRORS ─────────────────────────────────────

    class InvalidAttemptState : ClassroomDomainError()


    // ─── AUTHORIZATION ──────────────────────────────────────

    class TutorAccessDenied(
        val quizId: QuizId,
        val tutorId: String
    ) : ClassroomDomainError()
}

