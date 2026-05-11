package com.xapps.classroom.application.change

import com.xapps.classroom.domain.model.canonical__server_only.ClassroomQuiz

class VersionQuizStateChangeDetector : ClassroomQuizChangeDetector {

    override fun hasMeaningfulChange(
        existing: ClassroomQuiz?,
        updated: ClassroomQuiz
    ): Boolean {
        return existing == null ||
                existing.version != updated.version
    }
}