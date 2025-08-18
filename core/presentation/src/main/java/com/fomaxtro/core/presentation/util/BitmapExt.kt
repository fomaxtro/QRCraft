package com.fomaxtro.core.presentation.util

import android.graphics.Bitmap
import java.io.ByteArrayOutputStream

fun Bitmap.compressToByteArray(quality: Int = 85): ByteArray {
    val stream = ByteArrayOutputStream()

    compress(Bitmap.CompressFormat.JPEG, quality, stream)

    return stream.toByteArray()
}