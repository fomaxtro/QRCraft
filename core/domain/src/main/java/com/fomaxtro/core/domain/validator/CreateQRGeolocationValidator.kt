package com.fomaxtro.core.domain.validator

import com.fomaxtro.core.domain.util.ValidationError
import com.fomaxtro.core.domain.util.ValidationResult

class CreateQRGeolocationValidator {
    fun validateLatitude(latitude: String): ValidationResult<CreateQRGeolocationValidationError> {
        return when {
            latitude.isEmpty() -> {
                ValidationResult.Failure(CreateQRGeolocationValidationError.EMPTY_LATITUDE)
            }

            latitude.toDoubleOrNull() == null -> {
                ValidationResult.Failure(CreateQRGeolocationValidationError.INVALID_LATITUDE)
            }

            else -> ValidationResult.Success
        }
    }

    fun validateLongitude(longitude: String): ValidationResult<CreateQRGeolocationValidationError> {
        return when {
            longitude.isEmpty() -> {
                ValidationResult.Failure(CreateQRGeolocationValidationError.EMPTY_LONGITUDE)
            }

            longitude.toDoubleOrNull() == null -> {
                ValidationResult.Failure(CreateQRGeolocationValidationError.INVALID_LONGITUDE)
            }

            else -> ValidationResult.Success
        }
    }
}

enum class CreateQRGeolocationValidationError : ValidationError {
    EMPTY_LATITUDE,
    INVALID_LATITUDE,
    EMPTY_LONGITUDE,
    INVALID_LONGITUDE
}