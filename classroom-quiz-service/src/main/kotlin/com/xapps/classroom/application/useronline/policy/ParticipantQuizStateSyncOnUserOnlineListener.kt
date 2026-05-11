package com.xapps.classroom.application.useronline.policy

import com.xapps.classroom.application.port.out.PublishParticipantQuizStateRefreshEventPort
import com.xapps.classroom.domain.repository.ClassroomQuizRepository
import com.xapps.time.clock.ClockProvider
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import org.springframework.stereotype.Component

@Component
class ParticipantQuizStateSyncOnUserOnlineListener(
    private val repository: ClassroomQuizRepository,
    private val publisher: PublishParticipantQuizStateRefreshEventPort,
    private val clockProvider: ClockProvider
) : UserOnlinePolicy {

    override suspend fun execute(userId: String) {
        val participantQuizzes = repository.findQuizzesByParticipantUserId(userId).toList()
        if (participantQuizzes.isEmpty()) return

        val now = clockProvider.now()
        val syncableClassroomQuizIds = participantQuizzes
            .filter { quiz ->
                quiz.isWithinSyncWindow(now)
            }
            .map { it.id }

        if (syncableClassroomQuizIds.isNotEmpty()) {
            publisher.publishParticipantRefreshQuizzesEvent(
                participantId = userId,
                quizIds = syncableClassroomQuizIds
            )
        }
    }
}