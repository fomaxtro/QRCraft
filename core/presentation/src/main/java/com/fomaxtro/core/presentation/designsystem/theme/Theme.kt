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