package com.fomaxtro.core.presentation.screen.create_qr_geolocation

data class CreateQRGeolocationState(
    val latitude: String = "",
    val longitude: String = "",
    val canSubmit: Boolean = false,
    val isLoading: Boolean = false
)