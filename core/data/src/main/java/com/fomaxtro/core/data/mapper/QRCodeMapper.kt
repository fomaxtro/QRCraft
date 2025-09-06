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

fun QRCodeEntitySource.toQRCodeSource(): QRCodeSource {
    return when (this) {
        QRCodeEntitySource.GENERATED -> QRCodeSource.GENERATED
        QRCodeEntitySource.SCANNED -> QRCodeSource.SCANNED
    }
}

fun QRCodeEntity.toQRCodeEntry(
    qrParser: QRParser
) = QRCodeEntry(
    id = id,
    title = title,
    qrCode = qrParser.parseFromString(data),
    source = source.toQRCodeSource(),
    createdAt = Instant.ofEpochMilli(createdAt)
)