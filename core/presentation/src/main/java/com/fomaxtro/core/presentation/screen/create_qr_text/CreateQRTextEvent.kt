package com.fomaxtro.core.presentation.screen.create_qr_text

sealed interface CreateQRTextEvent {
    data class NavigateToScanResult(val qr: String) : CreateQRTextEvent
    data object NavigateBack : CreateQRTextEvent
}