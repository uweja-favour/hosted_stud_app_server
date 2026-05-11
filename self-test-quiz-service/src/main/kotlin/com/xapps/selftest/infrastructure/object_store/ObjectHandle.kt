package com.xapps.selftest.infrastructure.object_store

import java.io.InputStream

data class ObjectHandle(
    val metadata: ObjectMetadata,
    val streamProvider: () -> InputStream
)