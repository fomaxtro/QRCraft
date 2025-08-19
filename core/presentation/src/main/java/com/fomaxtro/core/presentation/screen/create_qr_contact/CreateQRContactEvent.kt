package com.fomaxtro.core.presentation.screen.create_qr_contact

import com.fomaxtro.core.presentation.model.QR

sealed interface CreateQRContactEvent {
    data class NavigateToScanResult(val qr: QR, val imagePath: String) : CreateQRContactEvent
    data object NavigateBack : CreateQRContactEvent
}