package com.fomaxtro.core.presentation.designsystem.cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fomaxtro.core.presentation.R
import com.fomaxtro.core.presentation.designsystem.buttons.QRCraftButton
import com.fomaxtro.core.presentation.designsystem.theme.QRCraftTheme
import com.fomaxtro.core.presentation.designsystem.theme.surfaceHigher

@Composable
fun QRCraftQRForm(
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier,
    canSubmit: Boolean = true,
    loading: Boolean = false,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentWidth()
            .width(480.dp)
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState()),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceHigher
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            content()

            Spacer(modifier = Modifier.height(8.dp))

            QRCraftButton(
                onClick = onSubmit,
                modifier = Modifier
                    .fillMaxWidth(),
                enabled = canSubmit,
                loading = loading
            ) {
                Text(stringResource(R.string.generate_qr_code))
            }
        }
    }
}

@Preview
@Composable
private fun QRCraftQRFormPreview() {
    QRCraftTheme {
        QRCraftQRForm(
            onSubmit = {},
            modifier = Modifier
                .width(480.dp)
        ) {
            OutlinedTextField(
                value = "",
                onValueChange = {},
                modifier = Modifier
                    .fillMaxWidth(),
                shape = MaterialTheme.shapes.large
            )
        }
    }
}