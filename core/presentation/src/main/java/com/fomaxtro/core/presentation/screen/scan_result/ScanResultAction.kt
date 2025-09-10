package com.fomaxtro.core.presentation.screen.scan_result

sealed interface ScanResultAction {
    data object OnNavigateBackClick : ScanResultAction
    data class OnShareClick(val text: String) : ScanResultAction
    data class OnCopyClick(val text: String) : ScanResultAction
}