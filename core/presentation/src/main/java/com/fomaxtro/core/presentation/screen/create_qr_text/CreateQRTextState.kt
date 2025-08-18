package com.fomaxtro.core.presentation.screen.create_qr_text

data class CreateQRTextState(
    val text: String = "",
    val canSubmit: Boolean = false,
    val isLoading: Boolean = false
)