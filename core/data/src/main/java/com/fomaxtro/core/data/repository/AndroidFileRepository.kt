package com.fomaxtro.core.data.repository

import android.content.ContentValues
import android.content.Context
import android.os.Environment
import android.provider.MediaStore
import com.fomaxtro.core.domain.model.ImageType
import com.fomaxtro.core.domain.repository.FileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AndroidFileRepository(
    private val context: Context
) : FileRepository {
    override suspend fun saveImageToDownloads(
        imageBytes: ByteArray,
        type: ImageType
    ): Boolean {
        val contentResolver = context.contentResolver
        val fileName = "qrcode_${System.currentTimeMillis()}.png"
        val mimeType = when (type) {
            ImageType.PNG -> "image/png"
        }

        val downloadDetails = ContentValues().apply {
            put(MediaStore.Downloads.MIME_TYPE, mimeType)
            put(MediaStore.Downloads.DISPLAY_NAME, fileName)
            put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
            put(MediaStore.Downloads.IS_PENDING, 1)
        }

        val downloadUri = contentResolver.insert(
            MediaStore.Downloads.EXTERNAL_CONTENT_URI,
            downloadDetails
        )

        return downloadUri?.let { downloadUri ->
            val operationResult = withContext(Dispatchers.IO) {
                contentResolver.openOutputStream(downloadUri)?.use { outputStream ->
                    outputStream.write(imageBytes)

                    true
                } ?: false
            }

            downloadDetails.clear()
            downloadDetails.put(MediaStore.Downloads.IS_PENDING, 0)

            contentResolver.update(downloadUri, downloadDetails, null, null)

            operationResult.also { success ->
                if (!success) {
                    contentResolver.delete(downloadUri, null)
                }
            }
        } ?: false
    }
}