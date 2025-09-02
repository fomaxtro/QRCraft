package com.fomaxtro.core.presentation.screen.create_qr_contact

sealed interface CreateQRContactEvent {
    data class NavigateToScanResult(val qr: String) : CreateQRContactEvent
    data object NavigateBack : CreateQRContactEvent
}