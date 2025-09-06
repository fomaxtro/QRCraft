package com.fomaxtro.core.presentation.screen.create_qr_wifi

import com.fomaxtro.core.domain.model.WifiEncryptionType

data class CreateQRWifiState(
    val ssid: String = "",
    val password: String = "",
    val encryptionType: WifiEncryptionType? = null,
    val canSubmit: Boolean = false,
    val isSubmitting: Boolean = false
)