package com.fomaxtro.core.domain.model

sealed interface QRCode {
    data class Text(val text: String) : QRCode
    data class Link(val url: String) : QRCode

    data class Contact(
        val name: String?,
        val phoneNumber: String?,
        val email: String?
    ) : QRCode

    data class PhoneNumber(val phoneNumber: String) : QRCode
    data class Geolocation(val latitude: Double, val longitude: Double) : QRCode

    data class Wifi(
        val ssid: String,
        val password: String?,
        val encryptionType: WifiEncryptionType
    ) : QRCode
}