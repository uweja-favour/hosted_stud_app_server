package com.xapps.classroom.application.useronline.policy

import com.xapps.classroom.application.port.out.PublishTutorQuizStateRefreshEventPort
import com.xapps.classroom.domain.repository.ClassroomQuizRepository
import com.xapps.time.clock.ClockProvider
import org.springframework.stereotype.Component
import kotlin.time.Duration.Companion.days

@Component
class TutorQuizStateSyncOnUserOnlineListener(
    private val repository: ClassroomQuizRepository,
    private val publisher: PublishTutorQuizStateRefreshEventPort,
    private val clockProvider: ClockProvider
) : UserOnlinePolicy {

    override suspend fun execute(userId: String) {
        val now = clockProvider.now()

        val tutorQuizzes = repository.findAllByTutorId(userId)
        if (tutorQuizzes.isEmpty()) return

        val syncableClassroomQuizIds = tutorQuizzes
            .filter { quiz ->
                quiz.isWithinSyncWindow(now)
            }
            .map { it.id }

        if (syncableClassroomQuizIds.isNotEmpty()) {
            publisher.publishTutorQuizStateRefreshEvent(
                tutorId = userId,
                quizIds = syncableClassroomQuizIds
            )
        }
    }

}