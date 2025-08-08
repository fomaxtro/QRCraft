package com.fomaxtro.core.presentation.screen.scan_result

sealed interface ScanResultEvent {
    data object NavigateBack : ScanResultEvent
    data class ShareText(val text: String) : ScanResultEvent
    data class CopyToClipboard(val text: String) : ScanResultEvent
}