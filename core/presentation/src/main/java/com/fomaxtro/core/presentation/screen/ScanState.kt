package com.fomaxtro.core.presentation.screen

data class ScanState(
    val hasCameraPermission: Boolean = false,
    val isScanning: Boolean = false,
    val showQrNotFoundDialog: Boolean = false
)
