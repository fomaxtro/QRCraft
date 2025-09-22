package com.fomaxtro.core.presentation.mapper

import com.fomaxtro.core.domain.model.QrCodeEntry
import com.fomaxtro.core.presentation.model.QrCodeUi
import java.time.ZoneId

fun QrCodeEntry.toQrCodeUi() = QrCodeUi(
    id = id,
    title = title,
    qrCode = qrCode,
    createdAt = createdAt.atZone(ZoneId.systemDefault())
)