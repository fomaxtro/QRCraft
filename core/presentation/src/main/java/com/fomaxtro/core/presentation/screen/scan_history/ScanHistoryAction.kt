package com.fomaxtro.core.presentation.screen.scan_history

sealed interface ScanHistoryAction {
    data class OnTabSelected(val tabIndex: Int) : ScanHistoryAction
}