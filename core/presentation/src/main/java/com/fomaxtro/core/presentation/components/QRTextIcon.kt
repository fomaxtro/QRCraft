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
import com.fomaxtro.core.presentation.designsystem.theme.text
import com.fomaxtro.core.presentation.designsystem.theme.textBg

@Composable
fun QRTextIcon(
    modifier: Modifier = Modifier
) {
    QRIconBox(
        containerColor = MaterialTheme.colorScheme.textBg,
        contentColor = MaterialTheme.colorScheme.text,
        modifier = modifier
    ) {
        Icon(
            imageVector = QRCraftIcons.Default.Text,
            contentDescription = stringResource(R.string.text)
        )
    }
}

@Preview
@Composable
private fun QRTextIconPreview() {
    QRCraftTheme {
        QRTextIcon()
    }
}