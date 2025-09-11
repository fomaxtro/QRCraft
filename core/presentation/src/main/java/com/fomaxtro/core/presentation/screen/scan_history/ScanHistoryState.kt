package com.fomaxtro.core.presentation.screen.scan_history

import com.fomaxtro.core.presentation.model.QRCodeUi

data class ScanHistoryState(
    val history: List<QRCodeUi> = emptyList(),
    val selectedTabIndex: Int = 0,
    val isShareSheetVisible: Boolean = false
)