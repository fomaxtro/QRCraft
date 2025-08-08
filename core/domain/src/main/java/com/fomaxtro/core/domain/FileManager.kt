package com.fomaxtro.core.domain

interface FileManager {
    suspend fun saveImage(image: ByteArray): String
    suspend fun readImageAndDelete(path: String): ByteArray
}