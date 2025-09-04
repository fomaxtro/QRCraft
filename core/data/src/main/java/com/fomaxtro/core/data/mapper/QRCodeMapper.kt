package com.fomaxtro.core.data.mapper

import com.fomaxtro.core.data.database.entity.QRCodeEntitySource
import com.fomaxtro.core.domain.model.QRCodeSource

fun QRCodeSource.toQRCodeEntitySource(): QRCodeEntitySource {
    return when (this) {
        QRCodeSource.GENERATED -> QRCodeEntitySource.GENERATED
        QRCodeSource.SCANNED -> QRCodeEntitySource.SCANNED
    }
}