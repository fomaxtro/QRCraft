package com.fomaxtro.core.presentation.designsystem.buttons

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.fomaxtro.core.presentation.designsystem.theme.onSurfaceDisabled

object QRCraftButtonDefaults {
    @Composable
    fun favouriteIconButtonColors(
        checked: Color = MaterialTheme.colorScheme.onSurface,
        unchecked: Color = MaterialTheme.colorScheme.onSurfaceDisabled
    ) = QRCraftFavouriteIconButtonColors(
        checked = checked,
        unchecked = unchecked
    )
}