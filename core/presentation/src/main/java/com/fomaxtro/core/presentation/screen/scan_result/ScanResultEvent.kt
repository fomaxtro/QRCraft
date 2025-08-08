package com.fomaxtro.core.presentation.screen.scan_result

sealed interface ScanResultEvent {
    data object NavigateBack : ScanResultEvent
    data class CopyToClipboard(val text: String) : ScanResultEvent
}