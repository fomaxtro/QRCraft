package com.fomaxtro.core.presentation.screen.create_qr_text

sealed interface CreateQRTextAction {
    data class OnTextChange(val text: String) : CreateQRTextAction
    data object OnSubmitClick : CreateQRTextAction
}