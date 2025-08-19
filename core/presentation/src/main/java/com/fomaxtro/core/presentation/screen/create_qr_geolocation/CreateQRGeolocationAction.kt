package com.fomaxtro.core.presentation.screen.create_qr_geolocation

sealed interface CreateQRGeolocationAction {
    data class OnLatitudeChanged(val latitude: String) : CreateQRGeolocationAction
    data class OnLongitudeChanged(val longitude: String) : CreateQRGeolocationAction
    data object OnSubmitClick : CreateQRGeolocationAction
    data object OnNavigateBackClick : CreateQRGeolocationAction
}