package com.fomaxtro.core.presentation.screen.create_qr_wifi

import com.fomaxtro.core.presentation.ui.UiText

sealed interface CreateQRWifiEvent {
    data class NavigateToScanResult(val id: Long) : CreateQRWifiEvent
    data object NavigateBack : CreateQRWifiEvent
    data class ShowSystemMessage(val message: UiText) : CreateQRWifiEvent
}