package com.xapps.selftest.application.generation

import com.xapps.dto.FileUploadDTO
import com.xapps.model.mime
import com.xapps.selftest.infrastructure.object_store.ObjectKey
import com.xapps.selftest.infrastructure.object_store.ObjectStore
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.springframework.stereotype.Component

@Component
class FileProcessor(
    private val objectStore: ObjectStore
) {

    suspend fun uploadFiles(
        files: List<FileUploadDTO>
    ): List<ObjectKey> = coroutineScope {
        files.map { dto ->
            async {
                objectStore.put(
                    fileName = dto.fileName,
                    mimeType = dto.mime(),
                    bytes = dto.encodedFile.bytes
                ).key   // PutResult.key is the FileKey the consumer service will use to retrieve
            }
        }.awaitAll()
    }

}