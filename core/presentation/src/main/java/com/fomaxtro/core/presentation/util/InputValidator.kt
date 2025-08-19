package com.fomaxtro.core.presentation.util

object InputValidator {
    fun isValidInputNumber(input: String): Boolean {
        return (input + "0").toDoubleOrNull() != null
    }
}