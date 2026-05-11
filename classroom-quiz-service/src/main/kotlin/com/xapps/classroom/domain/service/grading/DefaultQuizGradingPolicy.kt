package com.xapps.classroom.domain.service.grading

import com.xapps.model.Difficulty
import com.xapps.model.attempt.evaluation.Grade
import com.xapps.model.question.Question
import org.springframework.stereotype.Component

@Component
class DefaultQuizGradingPolicy : QuizGradingPolicy {

    override fun grade(
        questions: List<Question>,
        percentage: Double
    ): Grade {

        require(questions.isNotEmpty()) {
            "Cannot grade with no questions"
        }

        require(percentage in 0.0..100.0) {
            "Percentage must be in range 0..100"
        }

        val difficulty = Difficulty.fromWeight(
            questions.map { it.difficulty.weight }.average()
        )

        val thresholds = thresholdsFor(difficulty)

        return thresholds
            .first { percentage >= it.minimumPercentage }
            .grade
    }

    private fun thresholdsFor(difficulty: Difficulty): List<GradeThreshold> =
        when (difficulty) {
            Difficulty.VERY_EASY -> veryEasy()
            Difficulty.EASY -> easy()
            Difficulty.MEDIUM -> medium()
            Difficulty.HARD -> hard()
            Difficulty.VERY_HARD -> veryHard()
        }

    private fun veryEasy() = listOf(
        GradeThreshold(98.0, Grade.A_PLUS),
        GradeThreshold(94.0, Grade.A),
        GradeThreshold(91.0, Grade.A_MINUS),
        GradeThreshold(88.0, Grade.B_PLUS),
        GradeThreshold(84.0, Grade.B),
        GradeThreshold(81.0, Grade.B_MINUS),
        GradeThreshold(78.0, Grade.C_PLUS),
        GradeThreshold(74.0, Grade.C),
        GradeThreshold(71.0, Grade.C_MINUS),
        GradeThreshold(68.0, Grade.D_PLUS),
        GradeThreshold(64.0, Grade.D),
        GradeThreshold(61.0, Grade.D_MINUS),
        GradeThreshold(50.0, Grade.E),
        GradeThreshold(0.0, Grade.F)
    )

    private fun easy() = listOf(
        GradeThreshold(97.0, Grade.A_PLUS),
        GradeThreshold(93.0, Grade.A),
        GradeThreshold(90.0, Grade.A_MINUS),
        GradeThreshold(87.0, Grade.B_PLUS),
        GradeThreshold(83.0, Grade.B),
        GradeThreshold(80.0, Grade.B_MINUS),
        GradeThreshold(77.0, Grade.C_PLUS),
        GradeThreshold(73.0, Grade.C),
        GradeThreshold(70.0, Grade.C_MINUS),
        GradeThreshold(67.0, Grade.D_PLUS),
        GradeThreshold(63.0, Grade.D),
        GradeThreshold(60.0, Grade.D_MINUS),
        GradeThreshold(50.0, Grade.E),
        GradeThreshold(0.0, Grade.F)
    )

    private fun medium() = listOf(
        GradeThreshold(96.0, Grade.A_PLUS),
        GradeThreshold(92.0, Grade.A),
        GradeThreshold(88.0, Grade.A_MINUS),
        GradeThreshold(84.0, Grade.B_PLUS),
        GradeThreshold(80.0, Grade.B),
        GradeThreshold(76.0, Grade.B_MINUS),
        GradeThreshold(72.0, Grade.C_PLUS),
        GradeThreshold(68.0, Grade.C),
        GradeThreshold(64.0, Grade.C_MINUS),
        GradeThreshold(60.0, Grade.D_PLUS),
        GradeThreshold(56.0, Grade.D),
        GradeThreshold(52.0, Grade.D_MINUS),
        GradeThreshold(45.0, Grade.E),
        GradeThreshold(0.0, Grade.F)
    )

    private fun hard() = listOf(
        GradeThreshold(95.0, Grade.A_PLUS),
        GradeThreshold(90.0, Grade.A),
        GradeThreshold(85.0, Grade.A_MINUS),
        GradeThreshold(80.0, Grade.B_PLUS),
        GradeThreshold(75.0, Grade.B),
        GradeThreshold(70.0, Grade.B_MINUS),
        GradeThreshold(65.0, Grade.C_PLUS),
        GradeThreshold(60.0, Grade.C),
        GradeThreshold(55.0, Grade.C_MINUS),
        GradeThreshold(50.0, Grade.D_PLUS),
        GradeThreshold(45.0, Grade.D),
        GradeThreshold(40.0, Grade.D_MINUS),
        GradeThreshold(35.0, Grade.E),
        GradeThreshold(0.0, Grade.F)
    )

    private fun veryHard() = listOf(
        GradeThreshold(94.0, Grade.A_PLUS),
        GradeThreshold(88.0, Grade.A),
        GradeThreshold(84.0, Grade.A_MINUS),
        GradeThreshold(78.0, Grade.B_PLUS),
        GradeThreshold(72.0, Grade.B),
        GradeThreshold(68.0, Grade.B_MINUS),
        GradeThreshold(62.0, Grade.C_PLUS),
        GradeThreshold(58.0, Grade.C),
        GradeThreshold(54.0, Grade.C_MINUS),
        GradeThreshold(48.0, Grade.D_PLUS),
        GradeThreshold(44.0, Grade.D),
        GradeThreshold(40.0, Grade.D_MINUS),
        GradeThreshold(30.0, Grade.E),
        GradeThreshold(0.0, Grade.F)
    )

    private data class GradeThreshold(
        val minimumPercentage: Double,
        val grade: Grade
    )
}
