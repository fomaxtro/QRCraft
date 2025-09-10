package com.fomaxtro.qrcraft.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface Route : NavKey {
    @Serializable
    data object Scan : Route

    @Serializable
    data class ScanResult(val id: Long) : Route

    @Serializable
    data object CreateQR : Route

    @Serializable
    data object CreateQRText : Route

    @Serializable
    data object CreateQRLink : Route

    @Serializable
    data object CreateQRContact : Route

    @Serializable
    data object CreateQRPhoneNumber : Route

    @Serializable
    data object CreateQRGeolocation : Route

    @Serializable
    data object CreateQRWifi : Route

    @Serializable
    data object ScanHistory : Route
}