package com.xapps.classroom.config

import com.xapps.classroom.application.change.VersionQuizStateChangeDetector
import com.xapps.classroom.application.change.ClassroomQuizChangeDetector
import com.xapps.classroom.application.port.out.PublishParticipantQuizStateRefreshEventPort
import com.xapps.classroom.application.port.out.PublishTutorQuizStateRefreshEventPort
import com.xapps.classroom.application.repository.NotifyingClassroomQuizRepository
import com.xapps.classroom.domain.repository.ClassroomQuizRepository
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary

@Configuration
class ClassroomQuizRepositoryConfig {

    @Bean
    fun changeDetector(): ClassroomQuizChangeDetector =
        VersionQuizStateChangeDetector()

    @Bean
    @Primary
    fun classroomQuizRepository(
        @Qualifier("baseClassroomQuizRepository") baseRepository: ClassroomQuizRepository,
        changeDetector: ClassroomQuizChangeDetector,
        tutorPublisher: PublishTutorQuizStateRefreshEventPort,
        participantPublisher: PublishParticipantQuizStateRefreshEventPort
    ): ClassroomQuizRepository {
        return NotifyingClassroomQuizRepository(
            delegate = baseRepository,
            changeDetector = changeDetector,
            tutorPublisher = tutorPublisher,
            participantPublisher = participantPublisher,
        )
    }
}