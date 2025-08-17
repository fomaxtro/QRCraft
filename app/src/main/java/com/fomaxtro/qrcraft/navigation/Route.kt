package com.fomaxtro.qrcraft.navigation

import androidx.navigation3.runtime.NavKey
import com.fomaxtro.core.presentation.model.QR
import kotlinx.serialization.Serializable

@Serializable
sealed interface Route : NavKey {
    @Serializable
    data object Scan : Route

    @Serializable
    data class ScanResult(val qr: QR, val imagePath: String) : Route

    @Serializable
    data object CreateQR : Route
}