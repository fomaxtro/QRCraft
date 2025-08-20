package com.fomaxtro.core.presentation.screen.create_qr_wifi

import com.fomaxtro.core.presentation.model.QR

sealed interface CreateQRWifiEvent {
    data class NavigateToScanResult(val qr: QR, val imagePath: String) : CreateQRWifiEvent
    data object NavigateBack : CreateQRWifiEvent
}