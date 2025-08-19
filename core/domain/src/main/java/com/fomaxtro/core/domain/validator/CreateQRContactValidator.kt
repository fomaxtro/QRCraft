package com.fomaxtro.core.domain.validator

import com.fomaxtro.core.domain.PatternMatching
import com.fomaxtro.core.domain.util.ValidationError
import com.fomaxtro.core.domain.util.ValidationResult

class CreateQRContactValidator(
    private val patternMatching: PatternMatching
) {
    fun validateName(name: String): ValidationResult<CreateQRContactValidationError> {
        return when {
            name.isEmpty() -> {
                ValidationResult.Failure(CreateQRContactValidationError.EMPTY_NAME)
            }

            else -> ValidationResult.Success
        }
    }

    fun validateEmail(email: String): ValidationResult<CreateQRContactValidationError> {
        return when {
            email.isEmpty() -> {
                ValidationResult.Failure(CreateQRContactValidationError.EMPTY_EMAIL)
            }

            !patternMatching.isEmail(email) -> {
                ValidationResult.Failure(CreateQRContactValidationError.INVALID_EMAIL)
            }

            else -> ValidationResult.Success
        }
    }

    fun validatePhoneNumber(phoneNumber: String): ValidationResult<CreateQRContactValidationError> {
        return when {
            phoneNumber.isEmpty() -> {
                ValidationResult.Failure(CreateQRContactValidationError.EMPTY_PHONE_NUMBER)
            }

            !patternMatching.isValidPhone(phoneNumber) -> {
                ValidationResult.Failure(CreateQRContactValidationError.INVALID_PHONE_NUMBER)
            }

            else -> ValidationResult.Success
        }
    }
}

enum class CreateQRContactValidationError : ValidationError {
    EMPTY_NAME,
    EMPTY_EMAIL,
    INVALID_EMAIL,
    EMPTY_PHONE_NUMBER,
    INVALID_PHONE_NUMBER
}