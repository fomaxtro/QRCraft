package com.fomaxtro.core.presentation.screen.create_qr_geolocation

import com.fomaxtro.core.presentation.model.QR

sealed interface CreateQRGeolocationEvent {
    data class NavigateToScanResult(val qr: QR, val imagePath: String) : CreateQRGeolocationEvent
    data object NavigateBack : CreateQRGeolocationEvent
}