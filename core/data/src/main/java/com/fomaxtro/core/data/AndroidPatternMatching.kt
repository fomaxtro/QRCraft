package com.fomaxtro.core.data

import android.util.Patterns
import com.fomaxtro.core.domain.PatternMatching

class AndroidPatternMatching : PatternMatching {
    override fun isUrl(url: String): Boolean {
        return Patterns.WEB_URL.matcher(url).matches()
    }

    override fun isEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    override fun isValidPhone(phone: String): Boolean {
        return Patterns.PHONE.matcher(phone).matches()
    }
}