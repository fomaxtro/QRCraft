package com.fomaxtro.core.presentation.screen.scan_result

sealed interface ScanResultAction {
    data object OnNavigateBackClick : ScanResultAction
}