package com.fomaxtro.core.presentation.screen.create_qr_link

data class CreateQRLinkState(
    val url: String = "",
    val canSubmit: Boolean = false,
    val isSubmitting: Boolean = false
)