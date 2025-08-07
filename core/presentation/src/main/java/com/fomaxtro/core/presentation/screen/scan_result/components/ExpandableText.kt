package com.fomaxtro.core.presentation.screen.scan_result.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fomaxtro.core.presentation.R
import com.fomaxtro.core.presentation.designsystem.theme.QRCraftTheme
import com.fomaxtro.core.presentation.designsystem.theme.onSurfaceDisabled

@Composable
fun ExpandableText(
    text: String,
    modifier: Modifier = Modifier
) {
    var isClickable by rememberSaveable {
        mutableStateOf(false)
    }
    var isExpanded by rememberSaveable {
        mutableStateOf(false)
    }

    Column(
        modifier = modifier
            .width(IntrinsicSize.Max),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = text,
            maxLines = if (isExpanded) Int.MAX_VALUE else 6,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth(),
            onTextLayout = { result ->
                if (result.hasVisualOverflow) {
                    isClickable = true
                }
            }
        )

        if (isClickable) {
            Text(
                text = if (isExpanded) {
                    stringResource(R.string.show_less)
                } else {
                    stringResource(R.string.show_more)
                },
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceDisabled,
                modifier = Modifier
                    .clickable(
                        onClick = {
                            isExpanded = !isExpanded
                        },
                        indication = null,
                        interactionSource = null
                    )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ExpandableTextPreview() {
    QRCraftTheme {
        ExpandableText(
            text = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum."
        )
    }
}