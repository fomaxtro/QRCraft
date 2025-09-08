package com.fomaxtro.core.presentation.screen.create_qr.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fomaxtro.core.presentation.components.QRPhoneNumberIcon
import com.fomaxtro.core.presentation.designsystem.theme.QRCraftTheme
import com.fomaxtro.core.presentation.designsystem.theme.surfaceHigher

@Composable
fun ClickableCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    text: String,
) {
    Card(
        modifier = modifier
            .clickable(onClick = onClick)
            .width(IntrinsicSize.Max)
            .height(IntrinsicSize.Max),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceHigher
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    vertical = 20.dp,
                    horizontal = 16.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            icon()

            Text(
                text = text,
                style = MaterialTheme.typography.titleSmall
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF2196F3)
@Composable
private fun ClickableCardPreview() {
    QRCraftTheme {
        ClickableCard(
            onClick = {},
            icon = {
                QRPhoneNumberIcon()
            },
            text = "Call",
            modifier = Modifier
                .width(196.dp)
        )
    }
}