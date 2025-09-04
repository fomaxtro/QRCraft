package com.fomaxtro.core.domain.model

import java.time.Instant

data class QRCodeEntry(
    val id: Long,
    val title: String,
    val qrCode: QRCode,
    val createdAt: Instant
)