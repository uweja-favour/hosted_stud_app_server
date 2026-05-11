package com.xapps.classroom.domain.model.participant.question

import com.xapps.model.Difficulty
import com.xapps.model.McQuestionContract
import com.xapps.model.Option
import com.xapps.model.QuestionType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("ParticipantMcQuestion")
data class ParticipantMcQuestion(
    override val id: String,
    override val text: String,
    override val number: Int,
    override val difficulty: Difficulty,
    override val topic: String,
    override val options: List<Option>
) : ParticipantQuestion(QuestionType.MC), McQuestionContract