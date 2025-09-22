package com.fomaxtro.core.domain.model

import java.time.Instant

data class QrCodeEntry(
    val id: Long = 0,
    val title: String?,
    val qrCode: QrCode,
    val source: QrCodeSource,
    val createdAt: Instant = Instant.now()
)