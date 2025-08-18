package com.fomaxtro.core.domain.validator

import com.fomaxtro.core.domain.util.ValidationError
import com.fomaxtro.core.domain.util.ValidationResult

class CreateQRTextValidator {
    fun validateText(text: String): ValidationResult<CreateQRTextValidationError> {
        return if (text.isEmpty()) {
            ValidationResult.Failure(CreateQRTextValidationError.EMPTY_TEXT)
        } else {
            ValidationResult.Success
        }
    }
}

enum class CreateQRTextValidationError : ValidationError {
    EMPTY_TEXT
}