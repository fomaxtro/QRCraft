package com.fomaxtro.core.presentation.screen.scan_history

import com.fomaxtro.core.presentation.model.QrCodeUi

sealed interface ScanHistoryAction {
    data class OnTabSelected(val tabIndex: Int) : ScanHistoryAction
    data class OnHistoryClick(val qrCode: QrCodeUi) : ScanHistoryAction
    data class OnHistoryLongClick(val qrCode: QrCodeUi) : ScanHistoryAction
    data object OnBottomSheetDismiss : ScanHistoryAction
    data object OnShareClick : ScanHistoryAction
    data object OnDeleteClick : ScanHistoryAction
    data class OnFavouriteChange(val id: Long, val favourite: Boolean) : ScanHistoryAction
}