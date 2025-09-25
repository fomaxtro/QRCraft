package com.fomaxtro.core.presentation.screen.scan_result

sealed interface ScanResultAction {
    data object OnNavigateBackClick : ScanResultAction
    data object OnShareClick : ScanResultAction
    data object OnCopyClick : ScanResultAction
    data class OnFavouriteToggle(val favourite: Boolean) : ScanResultAction
    data object OnSaveClick : ScanResultAction
}