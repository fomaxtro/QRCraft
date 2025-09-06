package com.fomaxtro.core.presentation.screen.scan_result.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.fomaxtro.core.presentation.designsystem.theme.QRCraftTheme
import com.fomaxtro.core.presentation.designsystem.theme.onSurfaceDisabled

@Composable
fun EditableTitle(
    modifier: Modifier = Modifier,
    placeholder: (@Composable () -> Unit)? = null,
    state: TextFieldState
) {
    val textStyle = MaterialTheme.typography.titleMedium
    val interactionSource = remember {
        MutableInteractionSource()
    }
    val isFocused by interactionSource.collectIsFocusedAsState()

    BasicTextField(
        modifier = modifier,
        state = state,
        decorator = { innerTextField ->
            Box(
                contentAlignment = Alignment.Center
            ) {
                if (state.text.isEmpty()) {
                    CompositionLocalProvider(
                        LocalTextStyle provides if (isFocused) {
                            textStyle.copy(
                                color = MaterialTheme.colorScheme.onSurfaceDisabled
                            )
                        } else textStyle
                    ) {
                        placeholder?.invoke()
                    }
                }

                innerTextField()
            }
        },
        textStyle = textStyle.copy(
            textAlign = TextAlign.Center
        ),
        interactionSource = interactionSource
    )
}

@Preview(showBackground = true)
@Composable
private fun EditableTitlePreview() {
    QRCraftTheme {
        EditableTitle(
            state = TextFieldState(),
            placeholder = {
                Text("Title")
            }
        )
    }
}