package com.fomaxtro.core.domain.qr

import com.fomaxtro.core.domain.PatternMatching
import com.fomaxtro.core.domain.model.QR
import com.fomaxtro.core.domain.model.WifiEncryptionType

class QRParser(
    patternMatching: PatternMatching
) {
    private val patterns = QRPatterns(patternMatching)

    fun parseFromString(content: String): QR {
        return when {
            patterns.isContact(content) -> parseContact(content)
            patterns.isGeolocation(content) -> parseGeolocation(content)
            patterns.isLink(content) -> parseLink(content)
            patterns.isPhoneNumber(content) -> parsePhoneNumber(content)
            patterns.isWifi(content) -> parseWifi(content)
            else -> QR.Text(content)
        }
    }

    private fun parseWifi(content: String): QR.Wifi {
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

        return QR.Wifi(
            encryptionType = encryptionType,
            ssid = ssid,
            password = password
        )
    }

    private fun parsePhoneNumber(content: String): QR.PhoneNumber {
        return QR.PhoneNumber(content)
    }

    private fun parseLink(content: String): QR.Link {
        return QR.Link(content)
    }

    private fun parseGeolocation(content: String): QR.Geolocation {
        val (latitude, longitude) = content
            .replace("geo:", "")
            .replace(" ", "")
            .split(",")

        return QR.Geolocation(
            latitude = latitude.toDoubleOrNull() ?: 0.0,
            longitude = longitude.toDoubleOrNull() ?: 0.0
        )
    }

    private fun parseContact(content: String): QR.Contact {
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

        return QR.Contact(
            name = name,
            phoneNumber = phoneNumber,
            email = email
        )
    }

    fun convertToString(qr: QR): String {
        return when (qr) {
            is QR.Contact -> {
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

            is QR.Geolocation -> "geo:${qr.latitude},${qr.longitude}"
            is QR.Link -> qr.url
            is QR.PhoneNumber -> qr.phoneNumber
            is QR.Text -> qr.text
            is QR.Wifi -> "WIFI:T:${qr.encryptionType};S:${qr.ssid};P:${qr.password};;"
        }
    }
}