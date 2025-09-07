package com.fomaxtro.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fomaxtro.core.presentation.designsystem.theme.QRCraftTheme
import com.fomaxtro.core.presentation.designsystem.theme.link
import com.fomaxtro.core.presentation.designsystem.theme.linkBg

@Composable
fun QRIconBox(
    modifier: Modifier = Modifier,
    containerColor: Color,
    contentColor: Color,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .background(
                color = containerColor,
                shape = CircleShape
            )
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        CompositionLocalProvider(
            LocalContentColor provides contentColor
        ) {
            content()
        }
    }
}

@Preview
@Composable
private fun QRIconBoxPreview() {
    QRCraftTheme {
        QRIconBox(
            containerColor = MaterialTheme.colorScheme.linkBg,
            contentColor = MaterialTheme.colorScheme.link
        ) {

        }
    }
}