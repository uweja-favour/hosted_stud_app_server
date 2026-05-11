package com.xapps.classroom.infrastructure.clock

import com.xapps.time.clock.ClockProvider
import com.xapps.time.types.KotlinInstant
import kotlinx.datetime.TimeZone
import org.springframework.stereotype.Component
import kotlin.time.Clock

@Component
class ClassroomClockProvider(
    private val zone: TimeZone = TimeZone.UTC
) : ClockProvider {

    override fun now(): KotlinInstant = Clock.System.now()

    override fun timeZone(): TimeZone = zone
}