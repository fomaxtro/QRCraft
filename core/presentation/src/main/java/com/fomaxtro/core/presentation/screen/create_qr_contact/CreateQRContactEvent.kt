package com.fomaxtro.core.presentation.screen.create_qr_contact

import com.fomaxtro.core.presentation.ui.UiText

sealed interface CreateQRContactEvent {
    data class NavigateToScanResult(val id: Long) : CreateQRContactEvent
    data object NavigateBack : CreateQRContactEvent
    data class ShowSystemMessage(val message: UiText) : CreateQRContactEvent
}