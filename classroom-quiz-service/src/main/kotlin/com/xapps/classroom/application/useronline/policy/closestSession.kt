package com.xapps.classroom.application.useronline.policy

import com.xapps.classroom.domain.model.canonical__server_only.ClassroomQuiz
import com.xapps.classroom.domain.model.canonical__server_only.ClassroomSession
import com.xapps.time.types.KotlinDuration
import com.xapps.time.types.KotlinInstant
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days

private fun List<ClassroomSession>.closestSession(
    now: KotlinInstant
): ClassroomSession {

    require(isNotEmpty()) { "Cannot determine closest session from empty list" }

    return minBy { session ->
        val timeline = session.timeline()

        when {
            // is this session in the future?
            now < timeline.startTime -> {
                timeline.startTime - now
            }

            // is this session in the past?
            now > timeline.endsAt -> {
                now - timeline.endsAt
            }

            else -> {
                // we are inside the session → distance = 0 (best possible)
                kotlin.time.Duration.ZERO
            }
        }
    }
}

private fun ClassroomSession.endDistanceFrom(now: KotlinInstant): KotlinDuration {
    val end = timeline().endsAt

    val distance = if (end >= now) {
        end - now
    } else {
        now - end
    }

    return distance
}

fun ClassroomQuiz.isWithinSyncWindow(now: KotlinInstant): Boolean {
    return sessions.closestSession(now)
        .endDistanceFrom(now) <= 30.days
}