package com.fomaxtro.core.presentation.mapper

import com.fomaxtro.core.domain.model.QRCode
import com.fomaxtro.core.domain.model.WifiEncryptionType
import com.google.mlkit.vision.barcode.common.Barcode

fun Barcode.toQRCode(): QRCode {
    return when (valueType) {
        Barcode.TYPE_WIFI -> {
            val wifi = requireNotNull(wifi)

            QRCode.Wifi(
                ssid = wifi.ssid!!,
                password = wifi.password,
                encryptionType = when (wifi.encryptionType) {
                    Barcode.WiFi.TYPE_WEP -> WifiEncryptionType.WEP
                    Barcode.WiFi.TYPE_WPA -> WifiEncryptionType.WPA
                    Barcode.WiFi.TYPE_OPEN -> WifiEncryptionType.OPEN
                    else -> throw IllegalArgumentException("Unknown wifi encryption type")
                }
            )
        }

        Barcode.TYPE_GEO -> {
            val geolocation = requireNotNull(geoPoint)

            QRCode.Geolocation(
                latitude = geolocation.lat,
                longitude = geolocation.lng
            )
        }

        Barcode.TYPE_PHONE -> {
            QRCode.PhoneNumber(
                phoneNumber = requireNotNull(phone?.number)
            )
        }

        Barcode.TYPE_CONTACT_INFO -> {
            val contact = requireNotNull(contactInfo)

            val name = contact.name
            val phoneNumber = contact.phones.firstOrNull()
            val email = contact.emails.firstOrNull()

            QRCode.Contact(
                name = name?.formattedName,
                phoneNumber = phoneNumber?.number,
                email = email?.address
            )
        }

        Barcode.TYPE_URL -> {
            QRCode.Link(
                url = requireNotNull(url?.url)
            )
        }

        else -> QRCode.Text(rawValue ?: "")
    }
}

fun QRCode.toFormattedText(): String {
    return when (this) {
        is QRCode.Contact -> {
            buildString {
                if (name != null) {
                    appendLine(name)
                }

                if (phoneNumber != null) {
                    appendLine(phoneNumber)
                }

                if (email != null) {
                    append(email)
                }
            }
        }

        is QRCode.Geolocation -> {
            "$latitude, $longitude"
        }

        is QRCode.Link -> url
        is QRCode.PhoneNumber -> phoneNumber
        is QRCode.Text -> text

        is QRCode.Wifi -> {
            buildString {
                appendLine("SSID: $ssid")

                if (!password.isNullOrEmpty()) {
                    appendLine("Password: $password")
                }

                append("Encryption type: $encryptionType")
            }
        }
    }
}
