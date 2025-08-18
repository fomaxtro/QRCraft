package com.fomaxtro.core.presentation.screen.create_qr_link

import com.fomaxtro.core.presentation.model.QR

sealed interface CreateQRLinkEvent {
    data class NavigateToScanResult(val qr: QR, val imagePath: String) : CreateQRLinkEvent
    data object NavigateBack : CreateQRLinkEvent
}