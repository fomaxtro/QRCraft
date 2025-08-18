package com.fomaxtro.core.domain.util

sealed interface ValidationResult<out E : ValidationError> {
    data object Success : ValidationResult<Nothing>
    data class Failure<E : ValidationError>(val error: E) : ValidationResult<E>
}