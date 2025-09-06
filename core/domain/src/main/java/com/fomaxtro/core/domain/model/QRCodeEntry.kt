package com.fomaxtro.core.domain.model

import java.time.Instant

data class QRCodeEntry(
    val id: Long = 0,
    val title: String?,
    val qrCode: QRCode,
    val source: QRCodeSource,
    val createdAt: Instant = Instant.now()
)