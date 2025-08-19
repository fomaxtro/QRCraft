package com.fomaxtro.core.presentation.screen.create_qr_contact

data class CreateQRContactState(
    val name: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val canSubmit: Boolean = false,
    val isLoading: Boolean = false
)