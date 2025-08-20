package com.fomaxtro.core.domain.validator

import com.fomaxtro.core.domain.util.ValidationError
import com.fomaxtro.core.domain.util.ValidationResult

class CreateQRWifiValidator {
    fun validateSsid(ssid: String): ValidationResult<CreateQRWifiValidationError> {
        return if (ssid.isEmpty()) {
            ValidationResult.Failure(CreateQRWifiValidationError.EMPTY_SSID)
        } else {
            ValidationResult.Success
        }
    }

    fun validatePassword(password: String): ValidationResult<CreateQRWifiValidationError> {
        return if (password.isEmpty()) {
            ValidationResult.Failure(CreateQRWifiValidationError.EMPTY_PASSWORD)
        } else {
            ValidationResult.Success
        }
    }

    fun validateEncryptionType(
        encryptionType: Any?
    ): ValidationResult<CreateQRWifiValidationError> {
        return if (encryptionType == null) {
            ValidationResult.Failure(CreateQRWifiValidationError.EMPTY_ENCRYPTION_TYPE)
        } else {
            ValidationResult.Success
        }
    }
}

enum class CreateQRWifiValidationError : ValidationError {
    EMPTY_SSID,
    EMPTY_PASSWORD,
    EMPTY_ENCRYPTION_TYPE
}