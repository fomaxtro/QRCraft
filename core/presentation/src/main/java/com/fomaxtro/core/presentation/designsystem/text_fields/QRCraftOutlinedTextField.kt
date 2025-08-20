package com.fomaxtro.core.presentation.designsystem.text_fields

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldColors
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
    placeholder: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    colors: TextFieldColors = QRCraftOutlinedTextFieldDefaults.colors(),
    readOnly: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        shape = MaterialTheme.shapes.large,
        colors = colors,
        placeholder = placeholder,
        keyboardOptions = keyboardOptions,
        trailingIcon = trailingIcon,
        readOnly = readOnly
    )
}

object QRCraftOutlinedTextFieldDefaults {
    @Composable
    fun colors(): TextFieldColors {
        return OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = MaterialTheme.colorScheme.surface,
            unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceAlt,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            focusedContainerColor = MaterialTheme.colorScheme.surface
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun exposedDropdownMenuColors(): TextFieldColors {
        return ExposedDropdownMenuDefaults.outlinedTextFieldColors(
            unfocusedBorderColor = MaterialTheme.colorScheme.surface,
            unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceAlt,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            focusedContainerColor = MaterialTheme.colorScheme.surface
        )
    }
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