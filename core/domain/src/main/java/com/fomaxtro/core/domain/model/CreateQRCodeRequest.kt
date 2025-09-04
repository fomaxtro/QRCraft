package com.fomaxtro.core.domain.model

data class CreateQRCodeRequest(
    val title: String,
    val qrCode: QRCode,
    val source: QRCodeSource
)
