package com.xapps.classroom.domain.service.evaluation

import com.xapps.model.attempt.evaluation.Evaluation
import com.xapps.model.attempt.record.AnswerRecord
import com.xapps.model.question.Question

interface QuizEvaluator {

    fun evaluate(
        attemptId: String,
        answers: List<AnswerRecord>,
        questions: List<Question>
    ): Evaluation
}
