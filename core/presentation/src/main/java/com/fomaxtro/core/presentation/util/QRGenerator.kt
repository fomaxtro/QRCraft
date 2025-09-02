package com.fomaxtro.core.presentation.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import qrcode.QRCode
import qrcode.color.Colors

object QRGenerator {
    suspend fun generate(qr: String): Bitmap {
        return withContext(Dispatchers.Default) {
            val generator = QRCode.ofSquares()
                .withColor(Colors.BLACK)
                .withMargin(32)
                .build(qr)

            val qrBytes = generator.renderToBytes()

            BitmapFactory.decodeByteArray(qrBytes, 0, qrBytes.size)
        }
    }
}