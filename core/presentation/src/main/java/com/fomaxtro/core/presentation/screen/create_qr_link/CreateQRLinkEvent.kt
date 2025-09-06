package com.fomaxtro.core.presentation.screen.create_qr_link

import com.fomaxtro.core.presentation.ui.UiText

sealed interface CreateQRLinkEvent {
    data class NavigateToScanResult(val id: Long) : CreateQRLinkEvent
    data object NavigateBack : CreateQRLinkEvent
    data class ShowSystemMessage(val message: UiText) : CreateQRLinkEvent
}