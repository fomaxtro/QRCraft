package com.fomaxtro.core.presentation.screen.create_qr_link

sealed interface CreateQRLinkAction {
    data class OnUrlChange(val url: String) : CreateQRLinkAction
    data object OnSubmitClick : CreateQRLinkAction
    data object OnNavigateBackClick : CreateQRLinkAction
}