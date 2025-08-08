package com.fomaxtro.core.presentation.screen.scan_result

import com.fomaxtro.core.presentation.model.QR

data class ScanResultState(
    val qr: QR = QR.Text("")
)