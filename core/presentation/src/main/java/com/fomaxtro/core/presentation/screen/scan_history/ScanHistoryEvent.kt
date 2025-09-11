package com.fomaxtro.core.presentation.screen.scan_history

import com.fomaxtro.core.presentation.ui.UiText

sealed interface ScanHistoryEvent {
    data class ShowSystemError(val message: UiText) : ScanHistoryEvent
    data class ShareTo(val text: UiText) : ScanHistoryEvent
}