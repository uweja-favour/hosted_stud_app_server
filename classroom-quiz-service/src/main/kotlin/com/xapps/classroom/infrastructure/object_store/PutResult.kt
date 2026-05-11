package com.xapps.classroom.infrastructure.object_store

data class PutResult(
    val key: ObjectKey,
    val metadata: ObjectMetadata
)