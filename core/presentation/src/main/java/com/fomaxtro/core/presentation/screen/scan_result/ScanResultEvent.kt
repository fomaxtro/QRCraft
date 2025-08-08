package com.fomaxtro.core.presentation.screen.scan_result

sealed interface ScanResultEvent {
    data object NavigateBack : ScanResultEvent
}