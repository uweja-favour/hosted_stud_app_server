package com.xapps.classroom.domain.model.participant.question

import com.xapps.model.Difficulty
import com.xapps.model.FibQuestionContract
import com.xapps.model.QuestionType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("ParticipantFibQuestion")
data class ParticipantFibQuestion(
    override val id: String,
    override val text: String,
    override val number: Int,
    override val difficulty: Difficulty,
    override val topic: String
) : ParticipantQuestion(QuestionType.FIB), FibQuestionContract