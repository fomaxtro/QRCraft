package com.fomaxtro.core.presentation.screen.scan_result

import com.fomaxtro.core.presentation.ui.UiText

sealed interface ScanResultEvent {
    data object NavigateBack : ScanResultEvent
    data class ShareTo(val text: UiText) : ScanResultEvent
    data class CopyToClipboard(val text: UiText) : ScanResultEvent
    data class ShowSystemMessage(val message: UiText) : ScanResultEvent
    data class ShowMessage(val message: UiText) : ScanResultEvent
}