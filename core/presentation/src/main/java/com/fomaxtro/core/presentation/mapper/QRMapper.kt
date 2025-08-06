package com.fomaxtro.core.presentation.mapper

import com.fomaxtro.core.presentation.model.QR
import com.fomaxtro.core.presentation.model.WifiEncryptionType
import com.google.mlkit.vision.barcode.common.Barcode

fun Barcode.toQR(): QR? {
    return when (valueType) {
        Barcode.TYPE_WIFI -> {
            val wifi = wifi ?: return null

            QR.Wifi(
                ssid = wifi.ssid ?: return null,
                password = wifi.password,
                encryptionType = when (wifi.encryptionType) {
                    Barcode.WiFi.TYPE_WEP -> WifiEncryptionType.WEP
                    Barcode.WiFi.TYPE_WPA -> WifiEncryptionType.WPA
                    Barcode.WiFi.TYPE_OPEN -> WifiEncryptionType.OPEN
                    else -> null
                }
            )
        }

        Barcode.TYPE_GEO -> {
            val geolocation = geoPoint ?: return null

            QR.Geolocation(
                latitude = geolocation.lat,
                longitude = geolocation.lng
            )
        }

        Barcode.TYPE_PHONE -> {
            val phoneNumber = phone?.number ?: return null

            QR.PhoneNumber(
                phoneNumber = phoneNumber
            )
        }

        Barcode.TYPE_CONTACT_INFO -> {
            val contact = contactInfo ?: return null

            val name = contact.name
            val phoneNumber = contact.phones.firstOrNull()
            val email = contact.emails.firstOrNull()

            if (name == null && phoneNumber == null && email == null) {
                return null
            }

            QR.Contact(
                name = name!!.formattedName,
                phoneNumber = phoneNumber!!.number,
                email = email!!.address
            )
        }

        Barcode.TYPE_URL -> {
            val url = url?.url ?: return null

            QR.Link(
                link = url
            )
        }

        else -> QR.Text(rawValue ?: "")
    }
}
