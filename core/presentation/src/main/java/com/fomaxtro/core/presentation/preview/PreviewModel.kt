package com.fomaxtro.core.presentation.preview

import com.fomaxtro.core.domain.model.QrCode
import com.fomaxtro.core.presentation.model.QrCodeUi
import java.time.ZonedDateTime

object PreviewModel {
    fun createQrCodeUi(qrCode: QrCode) = QrCodeUi(
        id = 1,
        title = "Foo",
        qrCode = qrCode,
        favourite = false,
        createdAt = ZonedDateTime.now()
    )
}