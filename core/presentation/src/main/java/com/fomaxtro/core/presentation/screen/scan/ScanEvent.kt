package com.fomaxtro.core.presentation.screen.scan

import com.fomaxtro.core.presentation.ui.UiText

sealed interface ScanEvent {
    data object CloseApp : ScanEvent
    data object RequestCameraPermission : ScanEvent
    data class ShowSnackbar(val message: UiText) : ScanEvent
}