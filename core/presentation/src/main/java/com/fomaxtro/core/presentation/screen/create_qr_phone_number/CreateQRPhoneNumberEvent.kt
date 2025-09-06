package com.fomaxtro.core.presentation.screen.create_qr_phone_number

import com.fomaxtro.core.presentation.ui.UiText

sealed interface CreateQRPhoneNumberEvent {
    data class NavigateToScanResult(val id: Long) : CreateQRPhoneNumberEvent
    data object NavigateBack : CreateQRPhoneNumberEvent
    data class ShowSystemMessage(val message: UiText) : CreateQRPhoneNumberEvent
}