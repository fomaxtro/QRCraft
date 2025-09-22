package com.fomaxtro.core.presentation.qr

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import qrcode.QRCode
import qrcode.color.Colors

object QrGenerator {
    suspend fun generate(qr: String): Bitmap {
        return withContext(Dispatchers.Default) {
            val generator = QRCode.Companion.ofSquares()
                .withColor(Colors.BLACK)
                .withMargin(64)
                .build(qr)

            val qrBytes = generator.renderToBytes()

            BitmapFactory.decodeByteArray(qrBytes, 0, qrBytes.size)
        }
    }
}