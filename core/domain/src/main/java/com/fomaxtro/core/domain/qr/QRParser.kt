package com.fomaxtro.core.domain.qr

import com.fomaxtro.core.domain.PatternMatching
import com.fomaxtro.core.domain.model.QrCode
import com.fomaxtro.core.domain.model.WifiEncryptionType

class QrParser(
    patternMatching: PatternMatching
) {
    private val patterns = QrPatterns(patternMatching)

    fun parseFromString(content: String): QrCode {
        return when {
            patterns.isContact(content) -> parseContact(content)
            patterns.isGeolocation(content) -> parseGeolocation(content)
            patterns.isLink(content) -> parseLink(content)
            patterns.isPhoneNumber(content) -> parsePhoneNumber(content)
            patterns.isWifi(content) -> parseWifi(content)
            else -> QrCode.Text(content)
        }
    }

    private fun parseWifi(content: String): QrCode.Wifi {
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

        return QrCode.Wifi(
            encryptionType = encryptionType,
            ssid = ssid,
            password = password
        )
    }

    private fun parsePhoneNumber(content: String): QrCode.PhoneNumber {
        return QrCode.PhoneNumber(content)
    }

    private fun parseLink(content: String): QrCode.Link {
        return QrCode.Link(content)
    }

    private fun parseGeolocation(content: String): QrCode.Geolocation {
        val (latitude, longitude) = content
            .replace("geo:", "")
            .replace(" ", "")
            .split(",")

        return QrCode.Geolocation(
            latitude = latitude.toDoubleOrNull() ?: 0.0,
            longitude = longitude.toDoubleOrNull() ?: 0.0
        )
    }

    private fun parseContact(content: String): QrCode.Contact {
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

        return QrCode.Contact(
            name = name,
            phoneNumber = phoneNumber,
            email = email
        )
    }

    fun convertToString(qr: QrCode): String {
        return when (qr) {
            is QrCode.Contact -> {
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

            is QrCode.Geolocation -> "geo:${qr.latitude},${qr.longitude}"
            is QrCode.Link -> qr.url
            is QrCode.PhoneNumber -> qr.phoneNumber
            is QrCode.Text -> qr.text
            is QrCode.Wifi -> "WIFI:T:${qr.encryptionType};S:${qr.ssid};P:${qr.password};;"
        }
    }
}