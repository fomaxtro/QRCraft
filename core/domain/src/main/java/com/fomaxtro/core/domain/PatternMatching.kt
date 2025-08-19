package com.fomaxtro.core.domain

interface PatternMatching {
    fun isUrl(url: String): Boolean
    fun isEmail(email: String): Boolean
    fun isValidPhone(phone: String): Boolean
}