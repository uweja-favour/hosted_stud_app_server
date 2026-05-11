@file: OptIn(ExperimentalTime::class)

package com.xapps.selftest.domain.model

import com.xapps.model.attempt.QuizAttempt
import com.xapps.model.question.Question
import com.xapps.model.Difficulty
import com.xapps.model.QuizContract
import com.xapps.model.QuizId
import com.xapps.model.QuizType
import com.xapps.time.types.KotlinInstant
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlin.collections.filterNot
import kotlin.collections.isNotEmpty
import kotlin.time.ExperimentalTime

@Serializable
data class SelfTestQuiz(
    override val id: QuizId,
    override val title: String,
    override val subject: String,
    override val topic: String?,
    override val description: String?,
    override val questions: List<Question>,
    @Contextual
    override val createdAt: KotlinInstant,
    val attempts: List<QuizAttempt>
) : QuizContract {

    override val quizType: QuizType = QuizType.SELF_TEST

    val difficulty: Difficulty
        get() = Difficulty.fromWeight(questions.averageOf { it.difficulty.weight })

    val lastAttempt: QuizAttempt? get() = attempts.maxByOrNull { it.attemptNumber }

    fun replaceAttempt(attempt: QuizAttempt): SelfTestQuiz {
        require(attempts.isNotEmpty()) { "Attempts cannot be empty." }
        require(attempts.any { it.id == attempt.id }) { "Attempt not found." }

        val updatedAttempts = attempts
            .filterNot { it.id == attempt.id }
            .plus(attempt)

        return copy(attempts = updatedAttempts)
    }

    fun countUnfinishedAttempts(): Int =
        attempts.count { it.isUnfinished() }

    fun unfinishedAttemptOrNull(): QuizAttempt? =
        attempts.firstOrNull { it.isUnfinished() }

    fun hasUnfinishedAttempt(): Boolean =
        unfinishedAttemptOrNull() != null
}

private inline fun <T> List<T>.averageOf(selector: (T) -> Double): Double =
    map { selector(it) }.average()

inline fun <T> List<T>.averageOfNotNull(selector: (T) -> Double?): Double =
    mapNotNull { selector(it) }.average()
