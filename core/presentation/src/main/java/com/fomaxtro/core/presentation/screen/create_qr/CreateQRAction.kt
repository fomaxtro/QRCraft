package com.fomaxtro.core.presentation.screen.create_qr

sealed interface CreateQRAction {
    data object OnTextClick : CreateQRAction
    data object OnLinkClick : CreateQRAction
    data object OnContactClick : CreateQRAction
    data object OnPhoneClick : CreateQRAction
    data object OnGeolocationClick : CreateQRAction
    data object OnWifiClick : CreateQRAction
}