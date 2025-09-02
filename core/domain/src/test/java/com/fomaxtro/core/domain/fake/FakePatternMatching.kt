package com.fomaxtro.core.domain.fake

import com.fomaxtro.core.domain.PatternMatching

class FakePatternMatching : PatternMatching {
    var isValidUrl = false
    var isValidEmail = false
    var isValidPhone = false

    override fun isUrl(url: String): Boolean {
        return isValidUrl
    }

    override fun isEmail(email: String): Boolean {
        return isValidEmail
    }

    override fun isValidPhone(phone: String): Boolean {
        return isValidPhone
    }
}