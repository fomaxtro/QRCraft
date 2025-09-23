package com.fomaxtro.core.presentation.screen.scan_result

sealed interface ScanResultAction {
    data object OnNavigateBackClick : ScanResultAction
    data object OnShareClick : ScanResultAction
    data object OnCopyClick : ScanResultAction
    data object OnFavouriteToggle : ScanResultAction
    data object OnSaveClick : ScanResultAction
}