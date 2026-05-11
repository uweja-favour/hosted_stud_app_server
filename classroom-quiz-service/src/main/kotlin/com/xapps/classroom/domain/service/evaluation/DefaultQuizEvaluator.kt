package com.xapps.classroom.domain.service.evaluation

import com.xapps.classroom.domain.service.grading.QuizGradingPolicy
import com.xapps.model.QuestionId
import com.xapps.model.attempt.evaluation.Evaluation
import com.xapps.model.attempt.evaluation.QuestionOutcome
import com.xapps.model.attempt.evaluation.Report
import com.xapps.model.attempt.evaluation.TopicAnalysis
import com.xapps.model.attempt.record.Answer
import com.xapps.model.attempt.record.AnswerRecord
import com.xapps.model.attempt.record.FibAnswer
import com.xapps.model.attempt.record.McAnswer
import com.xapps.model.attempt.record.MsAnswer
import com.xapps.model.attempt.record.TfAnswer
import com.xapps.model.question.FibQuestion
import com.xapps.model.question.McQuestion
import com.xapps.model.question.MsQuestion
import com.xapps.model.question.Question
import com.xapps.model.question.TfQuestion
import com.xapps.platform.core.string.generateUniqueId
import com.xapps.time.clock.ClockProvider
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class DefaultQuizEvaluator(
    private val gradingPolicy: QuizGradingPolicy,
    private val clockProvider: ClockProvider
) : QuizEvaluator {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun evaluate(
        attemptId: String,
        answers: List<AnswerRecord>,
        questions: List<Question>
    ): Evaluation {

        val evaluationId = generateUniqueId()
        val answerMap = answers.associateBy { it.questionId }

        val outcomes = questions.map { question ->
            val answer = answerMap[question.id]?.answer

            if (answer != null) {
                QuestionOutcome(
                    id = generateUniqueId(),
                    evaluationId = evaluationId,
                    questionId = question.id,
                    isCorrect = isCorrect(question, answer).also {
                        log.info("Is correct is: $it")
                    }
                )
            } else {
                QuestionOutcome(
                    id = generateUniqueId(),
                    evaluationId = evaluationId,
                    questionId = question.id,
                    isCorrect = false.also {
                        log.info("Is correct is: ${false}")
                    }
                )
            }
        }

        val correctCount = outcomes.count { it.isCorrect }

        val percentage = (correctCount.toDouble() / questions.size.toDouble()) * 100.0

        val grade = gradingPolicy.grade(
            questions = questions,
            percentage = percentage
        )

        val report = buildReport(
            evaluationId = evaluationId,
            questions = questions,
            answers = answerMap,
            outcomes = outcomes
        )

        return Evaluation(
            id = evaluationId,
            attemptId = attemptId,
            outcomes = outcomes,
            grade = grade,
            report = report,
            evaluatedAtMillis = clockProvider.now().toEpochMilliseconds(),
        )
    }

    private fun isCorrect(
        question: Question,
        answer: Answer
    ): Boolean {
        val isCorrect = when (question) {

            is McQuestion if answer is McAnswer -> {
                (question.correctOptionId == answer.selectedOptionId)
            }

            is MsQuestion if answer is MsAnswer ->
                question.correctOptionIds.toSet().containsAll(answer.selectedOptionsIds.toSet())
                        && question.correctOptionIds.toSet().size == answer.selectedOptionsIds.toSet().size

            is TfQuestion if answer is TfAnswer ->
                question.correctOptionId == answer.selectedOptionId

            is FibQuestion if answer is FibAnswer ->
                question.acceptableAnswers.any {
                    it.equals(answer.fibTextAnswer, ignoreCase = true)
                }

            else -> error("Question: $question --------------------------- Answer: $answer")
        }

        return isCorrect
    }

    private fun buildReport(
        evaluationId: String,
        questions: List<Question>,
        answers: Map<QuestionId, AnswerRecord>,
        outcomes: List<QuestionOutcome>
    ): Report {

        val reportId = generateUniqueId().take(36)
        val byTopic = questions.groupBy { it.topic }

        val topicAnalyses = byTopic.map { (topic, qs) ->
            val correct = qs.count { q ->
                outcomes.first { it.questionId == q.id }.isCorrect
            }

            val confidence = qs.mapNotNull {
                answers[it.id]?.answer?.confidence?.level
            }
                .average()
                .toInt()

            TopicAnalysis(
                id = generateUniqueId().take(36),
                reportId = reportId,
                topic = topic,
                accuracy = correct.toDouble() / qs.size,
                averageConfidence = confidence,
                questionCount = qs.size,
                correctCount = correct
            )
        }

        val totalQuestions = topicAnalyses.sumOf { it.questionCount }
        val totalCorrect = topicAnalyses.sumOf { it.correctCount }

        return Report(
            id = reportId,
            evaluationId = evaluationId,
            overallAccuracy =
                if (totalQuestions == 0) 0.0
                else totalCorrect.toDouble() / totalQuestions,
            overallConfidence =
                if (totalQuestions == 0) 0
                else topicAnalyses.sumOf { it.averageConfidence * it.questionCount } / totalQuestions,
            topicAnalysis = topicAnalyses
        )
    }
}
