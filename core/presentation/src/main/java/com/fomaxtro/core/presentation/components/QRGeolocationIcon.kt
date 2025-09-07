package com.fomaxtro.core.presentation.components

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.fomaxtro.core.presentation.R
import com.fomaxtro.core.presentation.designsystem.theme.QRCraftIcons
import com.fomaxtro.core.presentation.designsystem.theme.QRCraftTheme
import com.fomaxtro.core.presentation.designsystem.theme.geo
import com.fomaxtro.core.presentation.designsystem.theme.geoBg

@Composable
fun QRGeolocationIcon(
    modifier: Modifier = Modifier
) {
    QRIconBox(
        containerColor = MaterialTheme.colorScheme.geoBg,
        contentColor = MaterialTheme.colorScheme.geo,
        modifier = modifier
    ) {
        Icon(
            imageVector = QRCraftIcons.Geolocation,
            contentDescription = stringResource(R.string.geolocation)
        )
    }
}

@Preview
@Composable
private fun QRGeolocationIconPreview() {
    QRCraftTheme {
        QRGeolocationIcon()
    }
}