package com.xapps.selftest.domain.factory

import com.xapps.dto.mappers.toQuestion
import com.xapps.model.QuizId
import com.xapps.questions.contracts.self_test_generation.dto.QuestionDTO
import com.xapps.selftest.domain.model.SelfTestQuiz
import com.xapps.time.types.KotlinInstant
import org.springframework.stereotype.Component

@Component
class SelfTestQuizFactory {

    fun create(
        quizId: QuizId,
        subject: String,
        title: String,
        now: KotlinInstant,
        questions: List<QuestionDTO>
    ): SelfTestQuiz {

        return SelfTestQuiz(
            id = quizId,
            title = title,
            subject = subject,
            topic = null,
            description = null,
            questions =  questions.mapIndexed { index, dTO ->
                dTO.toQuestion(quizId, index + 1)
            },
            createdAt = now,
            attempts = emptyList()
        )
    }
}
