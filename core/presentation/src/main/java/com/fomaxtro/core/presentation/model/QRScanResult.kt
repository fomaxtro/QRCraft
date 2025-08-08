package com.fomaxtro.core.presentation.model

import android.graphics.Bitmap

data class QRScanResult(
    val qr: QR,
    val image: Bitmap
)
