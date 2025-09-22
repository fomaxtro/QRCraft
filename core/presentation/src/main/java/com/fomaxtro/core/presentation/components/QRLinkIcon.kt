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
import com.fomaxtro.core.presentation.designsystem.theme.link
import com.fomaxtro.core.presentation.designsystem.theme.linkBg

@Composable
fun QRLinkIcon(
    modifier: Modifier = Modifier
) {
    QRIconBox(
        containerColor = MaterialTheme.colorScheme.linkBg,
        contentColor = MaterialTheme.colorScheme.link,
        modifier = modifier
    ) {
        Icon(
            imageVector = QRCraftIcons.Default.Link,
            contentDescription = stringResource(R.string.link)
        )
    }
}

@Preview
@Composable
private fun QRLinkIconPreview() {
    QRCraftTheme {
        QRLinkIcon()
    }
}