package com.fomaxtro.core.presentation.screen.create_qr_contact

sealed interface CreateQRContactAction {
    data class OnNameChange(val name: String) : CreateQRContactAction
    data class OnEmailChange(val email: String) : CreateQRContactAction
    data class OnPhoneNumberChange(val phoneNumber: String) : CreateQRContactAction
    data object OnSubmitClick : CreateQRContactAction
    data object OnNavigateBackClick : CreateQRContactAction
}