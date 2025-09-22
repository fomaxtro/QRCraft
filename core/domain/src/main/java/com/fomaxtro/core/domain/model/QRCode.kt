package com.fomaxtro.core.domain.model

sealed interface QrCode {
    data class Text(val text: String) : QrCode
    data class Link(val url: String) : QrCode

    data class Contact(
        val name: String?,
        val phoneNumber: String?,
        val email: String?
    ) : QrCode

    data class PhoneNumber(val phoneNumber: String) : QrCode
    data class Geolocation(val latitude: Double, val longitude: Double) : QrCode

    data class Wifi(
        val ssid: String,
        val password: String?,
        val encryptionType: WifiEncryptionType
    ) : QrCode
}