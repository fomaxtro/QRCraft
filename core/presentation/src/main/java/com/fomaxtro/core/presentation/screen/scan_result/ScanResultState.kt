package com.fomaxtro.core.presentation.screen.scan_result

import androidx.compose.ui.graphics.ImageBitmap
import com.fomaxtro.core.domain.model.QRCode

data class ScanResultState(
    val qr: QRCode = QRCode.Text(""),
    val qrImage: ImageBitmap? = null,
    val isLoading: Boolean = true
)