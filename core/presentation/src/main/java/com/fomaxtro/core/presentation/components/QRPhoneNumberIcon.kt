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
import com.fomaxtro.core.presentation.designsystem.theme.phone
import com.fomaxtro.core.presentation.designsystem.theme.phoneBg

@Composable
fun QRPhoneNumberIcon(
    modifier: Modifier = Modifier
) {
    QRIconBox(
        containerColor = MaterialTheme.colorScheme.phoneBg,
        contentColor = MaterialTheme.colorScheme.phone,
        modifier = modifier
    ) {
        Icon(
            imageVector = QRCraftIcons.Default.Phone,
            contentDescription = stringResource(R.string.phone_number)
        )
    }
}

@Preview
@Composable
private fun QRPhoneNumberIconPreview() {
    QRCraftTheme {
        QRPhoneNumberIcon()
    }
}