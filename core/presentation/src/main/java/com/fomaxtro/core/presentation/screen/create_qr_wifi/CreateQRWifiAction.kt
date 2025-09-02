package com.fomaxtro.core.presentation.screen.create_qr_wifi

import com.fomaxtro.core.domain.model.WifiEncryptionType

sealed interface CreateQRWifiAction {
    data class OnSSIDChange(val ssid: String) : CreateQRWifiAction
    data class OnPasswordChange(val password: String) : CreateQRWifiAction
    data class OnEncryptionTypeSelected(val encryptionType: WifiEncryptionType) : CreateQRWifiAction
    data object OnSubmitClick : CreateQRWifiAction
    data object OnNavigateBackClick : CreateQRWifiAction
}