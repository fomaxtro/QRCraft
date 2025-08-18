package com.fomaxtro.core.presentation.util

import com.fomaxtro.core.presentation.model.QR
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import qrcode.QRCode
import qrcode.color.Colors

object QRGenerator {
    suspend fun generate(qr: QR): ByteArray {
        val generator = QRCode.ofSquares()
            .withColor(Colors.BLACK)
            .withMargin(32)
            .build(qr.asString())

        return withContext(Dispatchers.Default) {
            generator.renderToBytes()
        }
    }
}