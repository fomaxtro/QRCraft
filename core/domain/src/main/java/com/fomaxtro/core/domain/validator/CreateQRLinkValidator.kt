package com.fomaxtro.core.domain.validator

import com.fomaxtro.core.domain.PatternMatching
import com.fomaxtro.core.domain.util.ValidationError
import com.fomaxtro.core.domain.util.ValidationResult

class CreateQRLinkValidator(
    private val patternMatching: PatternMatching
) {
    fun validateUrl(url: String): ValidationResult<CreateQRLinkValidationError> {
        return when {
            url.isEmpty() -> ValidationResult.Failure(CreateQRLinkValidationError.EMPTY_URL)

            !patternMatching.isUrl(url) -> {
                ValidationResult.Failure(CreateQRLinkValidationError.INVALID_URL)
            }

            else -> ValidationResult.Success
        }
    }
}

enum class CreateQRLinkValidationError : ValidationError {
    EMPTY_URL,
    INVALID_URL
}