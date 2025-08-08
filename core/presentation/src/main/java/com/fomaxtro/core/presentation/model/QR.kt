package com.fomaxtro.core.presentation.model

sealed interface QR {
    data class Text(val text: String) : QR
    data class Link(val url: String) : QR
    data class Contact(
        val name: String?,
        val phoneNumber: String?,
        val email: String?
    ) : QR

    data class PhoneNumber(val phoneNumber: String) : QR
    data class Geolocation(val latitude: Double, val longitude: Double) : QR
    data class Wifi(
        val ssid: String,
        val password: String?,
        val encryptionType: WifiEncryptionType
    ) : QR
}

enum class WifiEncryptionType {
    WPA, WEP, OPEN
}