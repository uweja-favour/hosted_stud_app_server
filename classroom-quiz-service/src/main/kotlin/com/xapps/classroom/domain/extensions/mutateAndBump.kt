package com.xapps.classroom.domain.extensions

import com.xapps.classroom.domain.model.canonical__server_only.ClassroomQuiz

inline fun ClassroomQuiz.mutateAndBump(
    mutation: (ClassroomQuiz) -> ClassroomQuiz
): ClassroomQuiz {
    val mutated = mutation(this)

    // defensive: ensure we don’t double-increment accidentally
    return if (mutated.version == this.version) {
        mutated.copy(version = this.version + 1)
    } else {
        mutated
    }
}

inline fun ClassroomQuiz.mutateAndRequireBump(
    mutation: (ClassroomQuiz) -> ClassroomQuiz
): ClassroomQuiz {
    val mutated = mutation(this)

    require(mutated.version == this.version) {
        "Mutation must not modify version directly. Use mutateAndRequireBump to control versioning."
    }

    return mutated.copy(version = this.version + 1)
}