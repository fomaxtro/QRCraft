package com.fomaxtro.core.presentation.mapper

import com.fomaxtro.core.presentation.model.QR
import com.fomaxtro.core.presentation.model.WifiEncryptionType
import com.google.mlkit.vision.barcode.common.Barcode

fun Barcode.toQR(): QR {
    return when (valueType) {
        Barcode.TYPE_WIFI -> {
            val wifi = requireNotNull(wifi)

            QR.Wifi(
                ssid = wifi.ssid,
                password = wifi.password,
                encryptionType = when (wifi.encryptionType) {
                    Barcode.WiFi.TYPE_WEP -> WifiEncryptionType.WEP
                    Barcode.WiFi.TYPE_WPA -> WifiEncryptionType.WPA
                    Barcode.WiFi.TYPE_OPEN -> WifiEncryptionType.OPEN
                    else -> throw IllegalArgumentException("Unknown encryption type: ${wifi.encryptionType}")
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
            val phone = requireNotNull(phone)

            QR.PhoneNumber(
                phoneNumber = requireNotNull(phone.number)
            )
        }

        Barcode.TYPE_CONTACT_INFO -> {
            val contact = requireNotNull(contactInfo)
            val phoneNumber = contact.phones.firstOrNull()

            if (contact.name == null && phoneNumber == null) {
                throw IllegalArgumentException("Name and phone number are null")
            }

            QR.Contact(
                name = contact.name?.formattedName,
                phoneNumber = phoneNumber?.number,
                email = contact.emails.firstOrNull()?.address
            )
        }

        Barcode.TYPE_URL -> {
            val url = requireNotNull(url)

            QR.Link(
                link = requireNotNull(url.url)
            )
        }

        else -> QR.Text(rawValue ?: "")
    }
}
