package com.fomaxtro.core.presentation.screen.create_qr_phone_number

import com.fomaxtro.core.presentation.model.QR

sealed interface CreateQRPhoneNumberEvent {
    data class NavigateToScanResult(val qr: QR, val imagePath: String) : CreateQRPhoneNumberEvent
    data object NavigateBack : CreateQRPhoneNumberEvent
}