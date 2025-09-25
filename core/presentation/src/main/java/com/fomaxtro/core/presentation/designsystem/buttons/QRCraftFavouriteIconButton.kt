package com.fomaxtro.core.presentation.designsystem.buttons

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.fomaxtro.core.presentation.R
import com.fomaxtro.core.presentation.designsystem.theme.QRCraftIcons
import com.fomaxtro.core.presentation.designsystem.theme.QRCraftTheme

@Immutable
data class QRCraftFavouriteIconButtonColors(
    val checked: Color,
    val unchecked: Color
)

@Composable
fun QRCraftFavouriteIconButton(
    favourite: Boolean,
    onFavouriteChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    colors: QRCraftFavouriteIconButtonColors = QRCraftButtonDefaults.favouriteIconButtonColors()
) {
    IconButton(
        onClick = {
            onFavouriteChange(!favourite)
        },
        colors = IconButtonDefaults.iconButtonColors(
            contentColor = if (favourite) colors.checked else colors.unchecked
        ),
        modifier = modifier
    ) {
        Icon(
            imageVector = if (favourite) {
                QRCraftIcons.Filled.Star
            } else {
                QRCraftIcons.Outlined.Star
            },
            contentDescription = if (favourite) {
                stringResource(R.string.favourite)
            } else {
                stringResource(R.string.unfavorite)
            }
        )
    }
}

@Preview
@Composable
private fun QRCraftFavouriteIconButtonPreview() {
    QRCraftTheme {
        QRCraftFavouriteIconButton(
            favourite = true,
            onFavouriteChange = {}
        )
    }
}