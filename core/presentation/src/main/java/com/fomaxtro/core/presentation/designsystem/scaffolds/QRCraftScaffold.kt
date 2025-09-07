package com.fomaxtro.core.presentation.designsystem.scaffolds

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.fomaxtro.core.presentation.R
import com.fomaxtro.core.presentation.designsystem.theme.QRCraftTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QRCraftScaffold(
    modifier: Modifier = Modifier,
    title: String,
    onBackClick: (() -> Unit)? = null,
    content: @Composable (innerPadding: PaddingValues) -> Unit
) {
    Scaffold(
        modifier = modifier,
        content = content,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                navigationIcon = {
                    if (onBackClick != null) {
                        IconButton(
                            onClick = onBackClick
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                                contentDescription = stringResource(R.string.navigate_back)
                            )
                        }
                    }
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.surface
    )
}

@Preview
@Composable
private fun QRCraftScaffoldPreview() {
    QRCraftTheme {
        QRCraftScaffold(
            title = "TopBar",
            onBackClick = {}
        ) {

        }
    }
}