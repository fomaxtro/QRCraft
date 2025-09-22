package com.fomaxtro.core.presentation.screen.scan_history

import com.fomaxtro.core.presentation.model.QrCodeUi

data class ScanHistoryState(
    val history: List<QrCodeUi> = emptyList(),
    val selectedTabIndex: Int = 0,
    val isShareSheetVisible: Boolean = false
)