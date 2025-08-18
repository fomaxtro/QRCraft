package com.fomaxtro.core.presentation.util

import com.fomaxtro.core.presentation.model.QR

fun interface ScanResultNavigation {
    fun navigate(qr: QR, imagePath: String)
}