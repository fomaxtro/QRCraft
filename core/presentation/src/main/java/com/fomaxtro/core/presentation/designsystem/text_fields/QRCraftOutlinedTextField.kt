package com.fomaxtro.core.presentation.designsystem.text_fields

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fomaxtro.core.presentation.designsystem.theme.QRCraftTheme
import com.fomaxtro.core.presentation.designsystem.theme.onSurfaceAlt

@Composable
fun QRCraftOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: @Composable (() -> Unit)? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        shape = MaterialTheme.shapes.large,
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = MaterialTheme.colorScheme.surface,
            unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceAlt,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            focusedContainerColor = MaterialTheme.colorScheme.surface
        ),
        placeholder = placeholder
    )
}

@Preview(showBackground = true)
@Composable
private fun QRCraftOutlinedTextFieldPreview() {
    QRCraftTheme {
        QRCraftOutlinedTextField(
            value = "Foo Bar",
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
    }
}