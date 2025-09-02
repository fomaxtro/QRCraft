package com.fomaxtro.core.domain.qr

import com.fomaxtro.core.domain.PatternMatching

internal class QRPatterns(
    private val patternMatching: PatternMatching
) {
    fun isContact(content: String): Boolean {
        return content.startsWith("BEGIN:VCARD") && content.endsWith("END:VCARD")
    }

    fun isGeolocation(content: String): Boolean {
        return Regex("^geo:-?[0-9]+(\\.[0-9]+)*,-?[0-9]+(\\.[0-9]+)*$")
            .matches(content)
    }

    fun isLink(content: String): Boolean {
        return patternMatching.isUrl(content)
    }

    fun isPhoneNumber(content: String): Boolean {
        return patternMatching.isValidPhone(content)
    }

    fun isWifi(content: String): Boolean {
        val wifiRegex = Regex("T:(wpa|wep|open)", RegexOption.IGNORE_CASE)

        return content.startsWith("WIFI:")
                && content.contains(wifiRegex)
                && content.contains("S:")
    }
}