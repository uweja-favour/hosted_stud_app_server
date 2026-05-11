package com.xapps.classroom.infrastructure.object_store

import org.springframework.web.multipart.MultipartFile
import java.io.InputStream

interface ObjectStore {

    suspend fun put(
        bucket: String = "uploads",
        file: MultipartFile
    ): PutResult

    suspend fun put(
        bucket: String = "uploads",
        fileName: String,
        mimeType: String,
        bytes: ByteArray
    ): PutResult

    suspend fun putStream(
        bucket: String = "uploads",
        fileName: String,
        mimeType: String,
        stream: InputStream,
        knownSize: Long = -1
    ): PutResult

    suspend fun delete(
        bucket: String = "uploads",
        key: ObjectKey
    ): Boolean

    fun get(
        key: ObjectKey,
        bucket: String = "uploads"
    ): ObjectHandle?

    fun head(
        bucket: String = "uploads",
        key: ObjectKey
    ): ObjectMetadata?
}