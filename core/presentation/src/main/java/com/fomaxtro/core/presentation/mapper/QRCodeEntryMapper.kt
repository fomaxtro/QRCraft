package com.fomaxtro.core.presentation.mapper

import com.fomaxtro.core.domain.model.QRCodeEntry
import com.fomaxtro.core.presentation.model.QRCodeUi
import java.time.ZoneId

fun QRCodeEntry.toQRCodeUi() = QRCodeUi(
    id = id,
    title = title,
    qrCode = qrCode,
    createdAt = createdAt.atZone(ZoneId.systemDefault())
)