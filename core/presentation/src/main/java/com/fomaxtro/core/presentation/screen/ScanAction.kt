package com.fomaxtro.core.presentation.screen

sealed interface ScanAction {
    data class OnCameraPermissionGranted(val isGranted: Boolean) : ScanAction
}