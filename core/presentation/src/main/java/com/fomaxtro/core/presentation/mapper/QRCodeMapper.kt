package com.fomaxtro.core.presentation.mapper

import com.fomaxtro.core.domain.model.QRCode
import com.fomaxtro.core.domain.model.WifiEncryptionType
import com.fomaxtro.core.presentation.R
import com.fomaxtro.core.presentation.ui.UiText
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

fun QRCode.toFormattedUiText(): UiText {
    return when (this) {
        is QRCode.Contact -> {
            val contact = buildString {
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

            UiText.DynamicString(contact)
        }

        is QRCode.Geolocation -> UiText.DynamicString("$latitude, $longitude")

        is QRCode.Link -> UiText.DynamicString(url)
        is QRCode.PhoneNumber -> UiText.DynamicString(phoneNumber)
        is QRCode.Text -> UiText.DynamicString(text)

        is QRCode.Wifi -> {
            UiText.Chained(
                listOf(
                    UiText.DynamicString("SSID: $ssid"),
                    UiText.StringResource(R.string.wifi_password, arrayOf(password ?: "")),
                    UiText.StringResource(R.string.wifi_encryption_type, arrayOf(encryptionType))
                )
            )
        }
    }
}

