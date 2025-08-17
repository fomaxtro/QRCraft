package com.fomaxtro.core.presentation.screen.create_qr

sealed interface CreateQREvent {
    data object NavigateToCreateTextQR : CreateQREvent
    data object NavigateToCreateLinkQR : CreateQREvent
    data object NavigateToCreateContactQR : CreateQREvent
    data object NavigateToCreatePhoneQR : CreateQREvent
    data object NavigateToCreateGeolocationQR : CreateQREvent
    data object NavigateToCreateWifiQR : CreateQREvent
}