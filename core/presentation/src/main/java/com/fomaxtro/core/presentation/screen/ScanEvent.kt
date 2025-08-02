package com.fomaxtro.core.presentation.screen

sealed interface ScanEvent {
    data object CameraPermissionDenied : ScanEvent
}