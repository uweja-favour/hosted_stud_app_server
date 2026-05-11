package com.xapps.classroom.domain.model.tutor

import com.xapps.classroom.domain.model.client_only.ClientClassroomSession
import com.xapps.classroom.domain.model.client_only.ClientSessionParticipation
import com.xapps.model.QuizContract
import com.xapps.model.QuizId
import com.xapps.model.QuizType
import com.xapps.model.question.Question
import com.xapps.time.types.KotlinInstant
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class TutorClassroomQuiz(
    override val id: QuizId,
    override val title: String,
    override val subject: String,
    override val topic: String?,
    override val description: String?,

    val sessions: List<TutorSession>,
    override val questions: List<Question>,
    @Contextual override val createdAt: KotlinInstant
) : QuizContract {
    override val quizType: QuizType
        get() = QuizType.CLASSROOM
}

@Serializable
data class TutorSession(
    val session: ClientClassroomSession,
    val participations: List<ClientSessionParticipation>
) {

    fun submissionsCount(): Int =
        participations.count { it.attempt != null }
}
