package com.xapps.classroom.domain.service.grading

import com.xapps.model.attempt.evaluation.Grade
import com.xapps.model.question.Question

interface QuizGradingPolicy {

    fun grade(
        questions: List<Question>,
        percentage: Double
    ): Grade
}
