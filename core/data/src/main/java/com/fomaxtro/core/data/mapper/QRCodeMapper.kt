package com.fomaxtro.core.data.mapper

import com.fomaxtro.core.data.database.entity.QRCodeEntity
import com.fomaxtro.core.data.database.entity.QRCodeEntitySource
import com.fomaxtro.core.domain.model.QRCodeEntry
import com.fomaxtro.core.domain.model.QRCodeSource
import com.fomaxtro.core.domain.qr.QRParser
import java.time.Instant

fun QRCodeSource.toQRCodeEntitySource(): QRCodeEntitySource {
    return when (this) {
        QRCodeSource.GENERATED -> QRCodeEntitySource.GENERATED
        QRCodeSource.SCANNED -> QRCodeEntitySource.SCANNED
    }
}

fun QRCodeEntity.toQRCodeEntry(
    qrParser: QRParser
) = QRCodeEntry(
    id = id,
    title = title,
    qrCode = qrParser.parseFromString(data),
    createdAt = Instant.ofEpochMilli(createdAt)
)