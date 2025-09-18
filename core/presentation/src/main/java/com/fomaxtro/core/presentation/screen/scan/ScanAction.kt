package com.fomaxtro.core.presentation.screen.scan

import com.google.mlkit.vision.barcode.common.Barcode

sealed interface ScanAction {
    data object OnCameraPermissionGranted : ScanAction
    data object OnCloseAppClick : ScanAction
    data object OnGrantAccessClick : ScanAction
    data class OnQrScanned(val barcode: Barcode) : ScanAction
    data class OnFlashToggle(val isFlashActive: Boolean) : ScanAction
}