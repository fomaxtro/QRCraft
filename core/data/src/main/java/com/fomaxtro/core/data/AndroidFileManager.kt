package com.fomaxtro.core.data

import android.content.Context
import com.fomaxtro.core.domain.FileManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.util.UUID

class AndroidFileManager(
    private val context: Context
) : FileManager {
    override suspend fun saveImage(image: ByteArray): String {
        val filename = "temp_${UUID.randomUUID()}.jpg"
        val file = File(context.cacheDir, filename)

        withContext(Dispatchers.IO) {
            file.writeBytes(image)
        }

        return file.absolutePath
    }

    override suspend fun readImage(path: String): ByteArray {
        val file = File(path)

        return withContext(Dispatchers.IO) {
            file.readBytes()
        }
    }
}