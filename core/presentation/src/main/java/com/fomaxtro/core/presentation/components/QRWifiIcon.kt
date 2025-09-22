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
import com.fomaxtro.core.presentation.designsystem.theme.wifi
import com.fomaxtro.core.presentation.designsystem.theme.wifiBg

@Composable
fun QRWifiIcon(
    modifier: Modifier = Modifier
) {
    QRIconBox(
        containerColor = MaterialTheme.colorScheme.wifiBg,
        contentColor = MaterialTheme.colorScheme.wifi,
        modifier = modifier
    ) {
        Icon(
            imageVector = QRCraftIcons.Default.Wifi,
            contentDescription = stringResource(R.string.wifi)
        )
    }
}

@Preview
@Composable
private fun QRWifiIconPreview() {
    QRCraftTheme {
        QRWifiIcon()
    }
}