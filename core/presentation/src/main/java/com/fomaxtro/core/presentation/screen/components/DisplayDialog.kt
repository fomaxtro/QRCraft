package com.fomaxtro.core.presentation.screen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.fomaxtro.core.presentation.R
import com.fomaxtro.core.presentation.designsystem.theme.QRCraftTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisplayDialog(
    onDismissRequest: () -> Unit,
    icon: @Composable () -> Unit,
    text: String,
    properties: DialogProperties = DialogProperties()
) {
    BasicAlertDialog(
        onDismissRequest = onDismissRequest,
        properties = properties
    ) {
        Card(
            modifier = Modifier
                .width(IntrinsicSize.Max)
                .height(IntrinsicSize.Max)
        ) {
            CompositionLocalProvider(
                LocalContentColor provides MaterialTheme.colorScheme.error
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            horizontal = 32.dp,
                            vertical = 20.dp
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    icon()

                    Text(
                        text = text,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun DisplayDialogPreview() {
    QRCraftTheme {
        DisplayDialog(
            onDismissRequest = {},
            icon = {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.alert_triangle),
                    contentDescription = null
                )
            },
            text = stringResource(R.string.qr_not_found)
        )
    }
}