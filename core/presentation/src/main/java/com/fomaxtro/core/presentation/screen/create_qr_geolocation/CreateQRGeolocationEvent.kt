package com.fomaxtro.core.presentation.screen.create_qr_geolocation

import com.fomaxtro.core.presentation.ui.UiText

sealed interface CreateQRGeolocationEvent {
    data class NavigateToScanResult(val id: Long) : CreateQRGeolocationEvent
    data object NavigateBack : CreateQRGeolocationEvent
    data class ShowSystemMessage(val message: UiText) : CreateQRGeolocationEvent
}