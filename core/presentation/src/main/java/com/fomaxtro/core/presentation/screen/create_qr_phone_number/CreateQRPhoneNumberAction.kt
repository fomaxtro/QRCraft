package com.fomaxtro.core.presentation.screen.create_qr_phone_number

sealed interface CreateQRPhoneNumberAction {
    data class OnPhoneNumberChange(val phoneNumber: String) : CreateQRPhoneNumberAction
    data object OnSubmitClick : CreateQRPhoneNumberAction
    data object OnNavigateBackClick : CreateQRPhoneNumberAction
}