package com.fomaxtro.core.domain.repository

import com.fomaxtro.core.domain.model.ImageType

interface FileRepository {
    suspend fun saveImageToDownloads(imageBytes: ByteArray, type: ImageType): Boolean
}