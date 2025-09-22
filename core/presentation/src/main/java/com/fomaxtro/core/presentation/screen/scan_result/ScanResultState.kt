package com.fomaxtro.core.presentation.screen.scan_result

import androidx.compose.ui.graphics.ImageBitmap
import com.fomaxtro.core.domain.model.QrCode

data class ScanResultState(
    val qr: QrCode = QrCode.Text(""),
    val qrImage: ImageBitmap? = null,
    val isLoading: Boolean = true,
    val isFavourite: Boolean = false
)