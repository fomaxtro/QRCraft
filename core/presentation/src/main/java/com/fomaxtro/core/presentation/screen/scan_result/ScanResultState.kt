package com.fomaxtro.core.presentation.screen.scan_result

import androidx.compose.ui.graphics.ImageBitmap
import com.fomaxtro.core.domain.model.QR

data class ScanResultState(
    val qr: QR = QR.Text(""),
    val qrImage: ImageBitmap? = null
)