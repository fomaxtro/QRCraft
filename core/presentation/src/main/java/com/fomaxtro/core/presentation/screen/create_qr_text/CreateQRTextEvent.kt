package com.fomaxtro.core.presentation.screen.create_qr_text

import com.fomaxtro.core.presentation.ui.UiText

sealed interface CreateQRTextEvent {
    data class NavigateToScanResult(val id: Long) : CreateQRTextEvent
    data object NavigateBack : CreateQRTextEvent
    data class ShowSystemMessage(val message: UiText) : CreateQRTextEvent
}