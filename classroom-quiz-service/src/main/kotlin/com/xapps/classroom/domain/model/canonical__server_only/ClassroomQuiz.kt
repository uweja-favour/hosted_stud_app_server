@file: OptIn(ExperimentalTime::class)

package com.xapps.classroom.domain.model.canonical__server_only

import com.xapps.model.QuizContract
import com.xapps.model.QuizId
import com.xapps.model.QuizType
import com.xapps.model.question.Question
import com.xapps.time.types.KotlinInstant
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlin.time.ExperimentalTime

@Serializable
data class ClassroomQuiz(
    val sessions: List<ClassroomSession>,

    val tutorId: String,
    val tutorEmail: String,
    override val id: QuizId,
    override val title: String,
    override val subject: String,
    override val questions: List<Question>,
    override val topic: String?,
    override val description: String?,

    @Contextual override val createdAt: KotlinInstant,

    // Monotonically increasing counter representing meaningful changes to tutor-visible quiz state.
    // This is NOT a persistence/versioning concern, but a domain signal used to deterministically
    // indicate when the quiz has changed in a way that requires downstream consumers (e.g. tutor clients)
    // to refresh their view. The value must only be incremented at mutation points that affect
    // observable tutor state (e.g. participation changes, attempt submissions/evaluations).
    // It is relied upon by infrastructure (e.g. repository decorators) to trigger notifications
    // without performing brittle structural comparisons.
    val version: Long
) : QuizContract {
    override val quizType: QuizType = QuizType.CLASSROOM
}