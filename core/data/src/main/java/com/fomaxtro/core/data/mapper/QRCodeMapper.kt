package com.fomaxtro.core.data.mapper

import com.fomaxtro.core.data.database.entity.QrCodeEntity
import com.fomaxtro.core.data.database.entity.QrCodeEntitySource
import com.fomaxtro.core.domain.model.QrCodeEntry
import com.fomaxtro.core.domain.model.QrCodeSource
import com.fomaxtro.core.domain.qr.QrParser
import java.time.Instant

fun QrCodeSource.toQrCodeEntitySource(): QrCodeEntitySource {
    return when (this) {
        QrCodeSource.GENERATED -> QrCodeEntitySource.GENERATED
        QrCodeSource.SCANNED -> QrCodeEntitySource.SCANNED
    }
}

fun QrCodeEntitySource.toQrCodeSource(): QrCodeSource {
    return when (this) {
        QrCodeEntitySource.GENERATED -> QrCodeSource.GENERATED
        QrCodeEntitySource.SCANNED -> QrCodeSource.SCANNED
    }
}

fun QrCodeEntity.toQrCodeEntry(
    qrParser: QrParser
) = QrCodeEntry(
    id = id,
    title = title,
    qrCode = qrParser.parseFromString(data),
    source = source.toQrCodeSource(),
    createdAt = Instant.ofEpochMilli(createdAt)
)

fun QrCodeEntry.toQrCodeEntity(
    qrParser: QrParser
) = QrCodeEntity(
    id = id,
    title = title,
    data = qrParser.convertToString(qrCode),
    source = source.toQrCodeEntitySource(),
    createdAt = createdAt.toEpochMilli()
)