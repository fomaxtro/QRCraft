package com.fomaxtro.core.presentation.util

import android.graphics.Bitmap
import java.io.ByteArrayOutputStream

fun Bitmap.compressToByteArray(
    format: Bitmap.CompressFormat,
    quality: Int = 100
): ByteArray {
    return ByteArrayOutputStream().use { outputStream ->
        compress(format, quality, outputStream)

        outputStream.toByteArray()
    }
}