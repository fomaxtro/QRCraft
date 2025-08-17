package com.fomaxtro.core.presentation.designsystem.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Primary,
    surface = Surface,
    onSurface = OnSurface,
    error = Error
)

val ColorScheme.overlay: Color
    get() = Overlay
val ColorScheme.onOverlay: Color
    get() = OnOverlay
val ColorScheme.onSurfaceDisabled: Color
    get() = OnSurfaceDisabled
val ColorScheme.surfaceHigher: Color
    get() = SurfaceHigher
val ColorScheme.link: Color
    get() = Link
val ColorScheme.linkBg: Color
    get() = LinkBg
val ColorScheme.text: Color
    get() = Text
val ColorScheme.textBg: Color
    get() = TextBg
val ColorScheme.success: Color
    get() = Success
val ColorScheme.contact: Color
    get() = Contact
val ColorScheme.contactBg: Color
    get() = ContactBg
val ColorScheme.geo: Color
    get() = Geo
val ColorScheme.geoBg: Color
    get() = GeoBg
val ColorScheme.phone: Color
    get() = Phone
val ColorScheme.phoneBg: Color
    get() = PhoneBg
val ColorScheme.wifi: Color
    get() = Wifi
val ColorScheme.wifiBg: Color
    get() = WifiBg

@Composable
fun QRCraftTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}