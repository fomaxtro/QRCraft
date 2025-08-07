package com.fomaxtro.core.presentation.screen

data class ScanState(
    val hasCameraPermission: Boolean = false,
    val isProcessingQr: Boolean = false
)
