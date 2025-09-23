package com.fomaxtro.core.presentation.designsystem.buttons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fomaxtro.core.presentation.R
import com.fomaxtro.core.presentation.designsystem.theme.QRCraftIcons
import com.fomaxtro.core.presentation.designsystem.theme.QRCraftTheme
import com.fomaxtro.core.presentation.designsystem.theme.onSurfaceDisabled

@Composable
fun QRCraftButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    colors: ButtonColors = ButtonDefaults.buttonColors(
        contentColor = MaterialTheme.colorScheme.onSurface,
        disabledContainerColor = MaterialTheme.colorScheme.surface,
        disabledContentColor = MaterialTheme.colorScheme.onSurfaceDisabled
    ),
    enabled: Boolean = true,
    loading: Boolean = false,
    content: @Composable RowScope.() -> Unit
) {
    Button(
        onClick = {
            if (!loading) {
                onClick()
            }
        },
        colors = colors,
        modifier = modifier.defaultMinSize(minHeight = 44.dp),
        enabled = enabled
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (loading) {
                val height = ButtonDefaults.MinHeight - ButtonDefaults.ContentPadding.run {
                    calculateTopPadding() + calculateBottomPadding()
                }

                CircularProgressIndicator(
                    modifier = Modifier
                        .size(height),
                    color = colors.contentColor
                )
            } else {
                content()
            }
        }
    }
}

@Preview
@Composable
private fun QRCraftButtonPreview() {
    QRCraftTheme {
        QRCraftButton(
            onClick = {},
            loading = true
        ) {
            Icon(
                imageVector = QRCraftIcons.Default.Share,
                contentDescription = stringResource(R.string.share)
            )

            Text(
                text = stringResource(R.string.share)
            )
        }
    }
}