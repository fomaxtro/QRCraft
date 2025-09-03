package com.fomaxtro.core.domain.qr

import com.fomaxtro.core.domain.PatternMatching
import com.fomaxtro.core.domain.model.QRCode
import com.fomaxtro.core.domain.model.WifiEncryptionType

class QRParser(
    patternMatching: PatternMatching
) {
    private val patterns = QRPatterns(patternMatching)

    fun parseFromString(content: String): QRCode {
        return when {
            patterns.isContact(content) -> parseContact(content)
            patterns.isGeolocation(content) -> parseGeolocation(content)
            patterns.isLink(content) -> parseLink(content)
            patterns.isPhoneNumber(content) -> parsePhoneNumber(content)
            patterns.isWifi(content) -> parseWifi(content)
            else -> QRCode.Text(content)
        }
    }

    private fun parseWifi(content: String): QRCode.Wifi {
        val tokens = content
            .replace("WIFI:", "")
            .replace(";;", "")
            .split(";")

        val encryptionType = tokens
            .find { it.startsWith("T:") }
            ?.substringAfter("T:")
            ?.runCatching { WifiEncryptionType.valueOf(this) }
            ?.getOrNull()
            ?: WifiEncryptionType.OPEN

        val ssid = tokens
            .find { it.startsWith("S:") }
            ?.substringAfter("S:")
            ?: ""
        val password = tokens
            .find { it.startsWith("P:") }
            ?.substringAfter("P:")

        return QRCode.Wifi(
            encryptionType = encryptionType,
            ssid = ssid,
            password = password
        )
    }

    private fun parsePhoneNumber(content: String): QRCode.PhoneNumber {
        return QRCode.PhoneNumber(content)
    }

    private fun parseLink(content: String): QRCode.Link {
        return QRCode.Link(content)
    }

    private fun parseGeolocation(content: String): QRCode.Geolocation {
        val (latitude, longitude) = content
            .replace("geo:", "")
            .replace(" ", "")
            .split(",")

        return QRCode.Geolocation(
            latitude = latitude.toDoubleOrNull() ?: 0.0,
            longitude = longitude.toDoubleOrNull() ?: 0.0
        )
    }

    private fun parseContact(content: String): QRCode.Contact {
        val tokens = content.split("\n")
            .drop(1)
            .dropLast(1)

        val name = tokens
            .find { it.startsWith("N:") }
            ?.substringAfter("N:")
        val email = tokens
            .find { it.startsWith("EMAIL:") }
            ?.substringAfter("EMAIL:")
        val phoneNumber = tokens
            .find { it.startsWith("TEL:") }
            ?.substringAfter("TEL:")

        return QRCode.Contact(
            name = name,
            phoneNumber = phoneNumber,
            email = email
        )
    }

    fun convertToString(qr: QRCode): String {
        return when (qr) {
            is QRCode.Contact -> {
                buildString {
                    appendLine("BEGIN:VCARD")

                    if (qr.name != null) {
                        appendLine("N:${qr.name}")
                    }

                    if (qr.email != null) {
                        appendLine("EMAIL:${qr.email}")
                    }

                    if (qr.phoneNumber != null) {
                        appendLine("TEL:${qr.phoneNumber}")
                    }

                    append("END:VCARD")
                }
            }

            is QRCode.Geolocation -> "geo:${qr.latitude},${qr.longitude}"
            is QRCode.Link -> qr.url
            is QRCode.PhoneNumber -> qr.phoneNumber
            is QRCode.Text -> qr.text
            is QRCode.Wifi -> "WIFI:T:${qr.encryptionType};S:${qr.ssid};P:${qr.password};;"
        }
    }
}