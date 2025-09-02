package com.fomaxtro.core.presentation.screen.create_qr_geolocation

sealed interface CreateQRGeolocationEvent {
    data class NavigateToScanResult(val qr: String) : CreateQRGeolocationEvent
    data object NavigateBack : CreateQRGeolocationEvent
}