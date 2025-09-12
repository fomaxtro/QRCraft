package com.fomaxtro.core.presentation.screen.scan_history

import com.fomaxtro.core.presentation.model.QRCodeUi

sealed interface ScanHistoryAction {
    data class OnTabSelected(val tabIndex: Int) : ScanHistoryAction
    data class OnHistoryClick(val qrCode: QRCodeUi) : ScanHistoryAction
    data class OnHistoryLongClick(val qrCode: QRCodeUi) : ScanHistoryAction
    data object OnBottomSheetDismiss : ScanHistoryAction
    data object OnShareClick : ScanHistoryAction
    data object OnDeleteClick : ScanHistoryAction
}