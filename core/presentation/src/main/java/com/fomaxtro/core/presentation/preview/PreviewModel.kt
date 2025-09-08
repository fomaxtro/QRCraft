package com.fomaxtro.core.presentation.preview

import com.fomaxtro.core.domain.model.QRCode
import com.fomaxtro.core.presentation.model.QRCodeUi
import java.time.ZonedDateTime

object PreviewModel {
    fun createQRCodeUi(qrCode: QRCode) = QRCodeUi(
        id = 1,
        title = "Foo",
        qrCode = qrCode,
        createdAt = ZonedDateTime.now()
    )
}