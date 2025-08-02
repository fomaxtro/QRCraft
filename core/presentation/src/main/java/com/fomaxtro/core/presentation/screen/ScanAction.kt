package com.fomaxtro.core.presentation.screen

sealed interface ScanAction {
    data object OnCameraPermissionGranted : ScanAction
    data object OnCloseAppClick : ScanAction
    data object OnGrantAccessClick : ScanAction
}