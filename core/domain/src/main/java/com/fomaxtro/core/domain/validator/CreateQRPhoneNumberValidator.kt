package com.fomaxtro.core.domain.validator

import com.fomaxtro.core.domain.PatternMatching
import com.fomaxtro.core.domain.util.ValidationError
import com.fomaxtro.core.domain.util.ValidationResult

class CreateQRPhoneNumberValidator(
    private val patternMatching: PatternMatching
) {
    fun validatePhoneNumber(
        phoneNumber: String
    ): ValidationResult<CreateQRPhoneNumberValidationError> {
        return when {
            phoneNumber.isEmpty() -> {
                ValidationResult.Failure(CreateQRPhoneNumberValidationError.EMPTY_PHONE_NUMBER)
            }

            !patternMatching.isValidPhone(phoneNumber) -> {
                ValidationResult.Failure(CreateQRPhoneNumberValidationError.INVALID_PHONE_NUMBER)
            }

            else -> ValidationResult.Success
        }
    }
}

enum class CreateQRPhoneNumberValidationError : ValidationError {
    EMPTY_PHONE_NUMBER,
    INVALID_PHONE_NUMBER
}