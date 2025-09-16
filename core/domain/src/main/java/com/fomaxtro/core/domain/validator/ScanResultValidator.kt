package com.fomaxtro.core.domain.validator

class ScanResultValidator {
    fun isValidTitleLength(title: String): Boolean {
        return title.length <= 32
    }
}