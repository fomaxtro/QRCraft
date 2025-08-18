package com.fomaxtro.core.presentation.screen.create_qr_text

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fomaxtro.core.presentation.R
import com.fomaxtro.core.presentation.designsystem.cards.QRCraftQRForm
import com.fomaxtro.core.presentation.designsystem.text_fields.QRCraftOutlinedTextField
import com.fomaxtro.core.presentation.designsystem.theme.QRCraftTheme

@Composable
fun CreateQRTextRoot(
    viewModel: CreateQRTextViewModel = viewModel<CreateQRTextViewModel>()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    CreateQRTextScreen(
        onAction = viewModel::onAction,
        state = state
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreateQRTextScreen(
    onAction: (CreateQRTextAction) -> Unit = {},
    state: CreateQRTextState
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.text),
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {}
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = stringResource(R.string.navigate_back)
                        )
                    }
                },
            )
        },
        containerColor = MaterialTheme.colorScheme.surface
    ) { innerPadding ->
        QRCraftQRForm(
            onSubmit = {
                onAction(CreateQRTextAction.OnSubmitClick)
            },
            canSubmit = state.canSubmit,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth()
                .wrapContentWidth()
                .width(480.dp)
                .padding(horizontal = 16.dp)
                .imePadding()
        ) {
            QRCraftOutlinedTextField(
                value = state.text,
                onValueChange = {
                    onAction(CreateQRTextAction.OnTextChange(it))
                },
                modifier = Modifier
                    .fillMaxWidth(),
                placeholder = {
                    Text(stringResource(R.string.text))
                }
            )
        }
    }
}

@Preview
@Composable
private fun CreateQRTextScreenPreview() {
    QRCraftTheme {
        CreateQRTextScreen(
            state = CreateQRTextState(
                text = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum."
            )
        )
    }
}