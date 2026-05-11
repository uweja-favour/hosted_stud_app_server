package com.xapps.selftest.infrastructure.object_store

data class PutResult(
    val key: ObjectKey,
    val metadata: ObjectMetadata
)