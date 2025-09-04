package com.fomaxtro.core.presentation.screen.scan_result

import com.fomaxtro.core.presentation.ui.UiText

sealed interface ScanResultEvent {
    data object NavigateBack : ScanResultEvent
    data class ShareText(val text: UiText) : ScanResultEvent
    data class CopyToClipboard(val text: UiText) : ScanResultEvent
}