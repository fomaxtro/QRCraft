package com.fomaxtro.core.presentation.model

import com.fomaxtro.core.domain.model.QRCode
import java.time.ZonedDateTime

data class QRCodeUi(
    val id: Long,
    val title: String?,
    val qrCode: QRCode,
    val createdAt: ZonedDateTime
)
