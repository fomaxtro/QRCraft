package com.fomaxtro.core.presentation.screen.scan

import com.fomaxtro.core.presentation.ui.UiText

sealed interface ScanEvent {
    data object CloseApp : ScanEvent
    data object RequestCameraPermission : ScanEvent
    data class ShowMessage(val message: UiText) : ScanEvent
    data class NavigateToScanResult(val id: Long) : ScanEvent
    data class ShowSystemMessage(val message: UiText) : ScanEvent
    data class ToggleFlash(val isFlashActive: Boolean) : ScanEvent
}