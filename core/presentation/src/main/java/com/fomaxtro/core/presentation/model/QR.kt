package com.fomaxtro.core.presentation.model

import kotlinx.serialization.Serializable

@Serializable
sealed interface QR {
    @Serializable
    data class Text(val text: String) : QR

    @Serializable
    data class Link(val url: String) : QR

    @Serializable
    data class Contact(
        val name: String?,
        val phoneNumber: String?,
        val email: String?
    ) : QR

    @Serializable
    data class PhoneNumber(val phoneNumber: String) : QR

    @Serializable
    data class Geolocation(val latitude: Double, val longitude: Double) : QR

    @Serializable
    data class Wifi(
        val ssid: String,
        val password: String?,
        val encryptionType: WifiEncryptionType
    ) : QR

    fun asString(): String {
        return when (this) {
            is Contact -> TODO()
            is Geolocation -> TODO()
            is Link -> TODO()
            is PhoneNumber -> TODO()
            is Text -> text
            is Wifi -> TODO()
        }
    }
}

enum class WifiEncryptionType {
    WPA, WEP, OPEN
}