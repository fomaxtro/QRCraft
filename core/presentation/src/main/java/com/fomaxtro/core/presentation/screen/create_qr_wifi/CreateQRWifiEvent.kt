package com.fomaxtro.core.presentation.screen.create_qr_wifi

sealed interface CreateQRWifiEvent {
    data class NavigateToScanResult(val qr: String) : CreateQRWifiEvent
    data object NavigateBack : CreateQRWifiEvent
}