package com.fomaxtro.core.presentation.screen.create_qr_link

sealed interface CreateQRLinkEvent {
    data class NavigateToScanResult(val qr: String) : CreateQRLinkEvent
    data object NavigateBack : CreateQRLinkEvent
}