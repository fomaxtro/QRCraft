package com.fomaxtro.core.presentation.mapper

import com.fomaxtro.core.presentation.model.QR
import com.fomaxtro.core.presentation.model.WifiEncryptionType
import com.google.mlkit.vision.barcode.common.Barcode

fun Barcode.toQR(): QR {
    return when (valueType) {
        Barcode.TYPE_WIFI -> {
            val wifi = requireNotNull(wifi)

            QR.Wifi(
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

            QR.Geolocation(
                latitude = geolocation.lat,
                longitude = geolocation.lng
            )
        }

        Barcode.TYPE_PHONE -> {
            QR.PhoneNumber(
                phoneNumber = requireNotNull(phone?.number)
            )
        }

        Barcode.TYPE_CONTACT_INFO -> {
            val contact = requireNotNull(contactInfo)

            val name = contact.name
            val phoneNumber = contact.phones.firstOrNull()
            val email = contact.emails.firstOrNull()

            QR.Contact(
                name = name?.formattedName,
                phoneNumber = phoneNumber?.number,
                email = email?.address
            )
        }

        Barcode.TYPE_URL -> {
            QR.Link(
                url = requireNotNull(url?.url)
            )
        }

        else -> QR.Text(rawValue ?: "")
    }
}

fun QR.toFormattedText(): String {
    return when (this) {
        is QR.Contact -> {
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

        is QR.Geolocation -> {
            "$latitude, $longitude"
        }

        is QR.Link -> url
        is QR.PhoneNumber -> phoneNumber
        is QR.Text -> text

        is QR.Wifi -> {
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
