package com.xapps.classroom.domain.model.participant.question

import com.xapps.model.Difficulty
import com.xapps.model.MsQuestionContract
import com.xapps.model.Option
import com.xapps.model.QuestionType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("ParticipantMsQuestion")
data class ParticipantMsQuestion(
    override val id: String,
    override val text: String,
    override val number: Int,
    override val difficulty: Difficulty,
    override val topic: String,
    override val options: List<Option>
) : ParticipantQuestion(QuestionType.MS), MsQuestionContract