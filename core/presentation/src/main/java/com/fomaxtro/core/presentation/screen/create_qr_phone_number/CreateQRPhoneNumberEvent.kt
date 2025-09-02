package com.fomaxtro.core.presentation.screen.create_qr_phone_number

sealed interface CreateQRPhoneNumberEvent {
    data class NavigateToScanResult(val qr: String) : CreateQRPhoneNumberEvent
    data object NavigateBack : CreateQRPhoneNumberEvent
}