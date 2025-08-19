package com.fomaxtro.core.presentation.screen.create_qr_phone_number

data class CreateQRPhoneNumberState(
    val phoneNumber: String = "",
    val canSubmit: Boolean = false,
    val isLoading: Boolean = false
)