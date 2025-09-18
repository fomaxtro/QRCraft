package com.fomaxtro.core.presentation.screen.scan

data class ScanState(
    val hasCameraPermission: Boolean = false,
    val isProcessingQr: Boolean = false,
    val isFlashActive: Boolean = false
)
