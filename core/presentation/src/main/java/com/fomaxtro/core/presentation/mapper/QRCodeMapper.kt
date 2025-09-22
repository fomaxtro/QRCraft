package com.fomaxtro.core.presentation.mapper

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.fomaxtro.core.domain.model.QrCode
import com.fomaxtro.core.domain.model.WifiEncryptionType
import com.fomaxtro.core.presentation.R
import com.fomaxtro.core.presentation.ui.UiText
import com.google.mlkit.vision.barcode.common.Barcode

fun Barcode.toQrCode(): QrCode {
    return when (valueType) {
        Barcode.TYPE_WIFI -> {
            val wifi = requireNotNull(wifi)

            QrCode.Wifi(
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

            QrCode.Geolocation(
                latitude = geolocation.lat,
                longitude = geolocation.lng
            )
        }

        Barcode.TYPE_PHONE -> {
            QrCode.PhoneNumber(
                phoneNumber = requireNotNull(phone?.number)
            )
        }

        Barcode.TYPE_CONTACT_INFO -> {
            val contact = requireNotNull(contactInfo)

            val name = contact.name
            val phoneNumber = contact.phones.firstOrNull()
            val email = contact.emails.firstOrNull()

            QrCode.Contact(
                name = name?.formattedName,
                phoneNumber = phoneNumber?.number,
                email = email?.address
            )
        }

        Barcode.TYPE_URL -> {
            QrCode.Link(
                url = requireNotNull(url?.url)
            )
        }

        else -> QrCode.Text(rawValue ?: "")
    }
}

fun QrCode.toFormattedUiText(): UiText {
    return when (this) {
        is QrCode.Contact -> {
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

        is QrCode.Geolocation -> UiText.DynamicString("$latitude, $longitude")

        is QrCode.Link -> UiText.DynamicString(url)
        is QrCode.PhoneNumber -> UiText.DynamicString(phoneNumber)
        is QrCode.Text -> UiText.DynamicString(text)

        is QrCode.Wifi -> {
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

@Composable
fun QrCode.toTitle(): String {
    return when (this) {
        is QrCode.Contact -> stringResource(R.string.contact)
        is QrCode.Geolocation -> stringResource(R.string.geolocation)
        is QrCode.Link -> stringResource(R.string.link)
        is QrCode.PhoneNumber -> stringResource(R.string.phone_number)
        is QrCode.Text -> stringResource(R.string.text)
        is QrCode.Wifi -> stringResource(R.string.wifi)
    }
}
