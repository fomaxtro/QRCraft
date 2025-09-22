package com.fomaxtro.core.presentation.model

import com.fomaxtro.core.domain.model.QrCode
import java.time.ZonedDateTime

data class QrCodeUi(
    val id: Long,
    val title: String?,
    val qrCode: QrCode,
    val createdAt: ZonedDateTime
)
