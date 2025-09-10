package com.fomaxtro.core.presentation.screen.scan_result

import com.fomaxtro.core.presentation.ui.UiText

sealed interface ScanResultEvent {
    data object NavigateBack : ScanResultEvent
    data class ShowSystemMessage(val message: UiText) : ScanResultEvent
}