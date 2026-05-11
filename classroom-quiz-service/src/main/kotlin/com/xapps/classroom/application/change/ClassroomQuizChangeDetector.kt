package com.xapps.classroom.application.change

import com.xapps.classroom.domain.model.canonical__server_only.ClassroomQuiz

interface ClassroomQuizChangeDetector {
    fun hasMeaningfulChange(
        existing: ClassroomQuiz?,
        updated: ClassroomQuiz
    ): Boolean
}