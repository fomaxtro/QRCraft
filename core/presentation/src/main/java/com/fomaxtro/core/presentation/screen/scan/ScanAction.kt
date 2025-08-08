package com.fomaxtro.core.presentation.screen.scan

import com.fomaxtro.core.presentation.model.QRScanResult

sealed interface ScanAction {
    data object OnCameraPermissionGranted : ScanAction
    data object OnCloseAppClick : ScanAction
    data object OnGrantAccessClick : ScanAction
    data class OnQrScanned(val qrScanResult: QRScanResult) : ScanAction
}