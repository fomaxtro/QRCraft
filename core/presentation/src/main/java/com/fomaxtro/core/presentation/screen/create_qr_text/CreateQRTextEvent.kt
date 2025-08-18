package com.fomaxtro.core.presentation.screen.create_qr_text

import com.fomaxtro.core.presentation.model.QR

sealed interface CreateQRTextEvent {
    data class NavigateToScanResult(val qr: QR, val imagePath: String) : CreateQRTextEvent
    data object NavigateBack : CreateQRTextEvent
}