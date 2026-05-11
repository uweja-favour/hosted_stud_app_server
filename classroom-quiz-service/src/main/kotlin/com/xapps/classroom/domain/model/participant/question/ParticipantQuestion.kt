package com.xapps.classroom.domain.model.participant.question

import com.xapps.model.Difficulty
import com.xapps.model.QuestionContract
import com.xapps.model.QuestionType
import kotlinx.serialization.Serializable

@Serializable
sealed class ParticipantQuestion(override val questionType: QuestionType): QuestionContract {

    abstract override val id: String

    abstract override val text: String

    abstract override val difficulty: Difficulty

    abstract val topic: String
}