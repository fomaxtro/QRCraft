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
import com.fomaxtro.core.presentation.designsystem.theme.contact
import com.fomaxtro.core.presentation.designsystem.theme.contactBg

@Composable
fun QRContactIcon(
    modifier: Modifier = Modifier
) {
    QRIconBox(
        containerColor = MaterialTheme.colorScheme.contactBg,
        contentColor = MaterialTheme.colorScheme.contact,
        modifier = modifier
    ) {
        Icon(
            imageVector = QRCraftIcons.Contact,
            contentDescription = stringResource(R.string.contact)
        )
    }
}

@Preview
@Composable
private fun QRContactIconPreview() {
    QRCraftTheme {
        QRContactIcon()
    }
}