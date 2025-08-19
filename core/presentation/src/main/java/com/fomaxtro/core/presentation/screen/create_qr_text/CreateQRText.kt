package com.fomaxtro.core.presentation.screen.create_qr_text

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fomaxtro.core.presentation.R
import com.fomaxtro.core.presentation.designsystem.cards.QRCraftQRForm
import com.fomaxtro.core.presentation.designsystem.text_fields.QRCraftOutlinedTextField
import com.fomaxtro.core.presentation.designsystem.theme.QRCraftTheme
import com.fomaxtro.core.presentation.ui.ObserveAsEvents
import com.fomaxtro.core.presentation.util.ScanResultNavigation
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CreateQRTextRoot(
    navigateToScanResult: ScanResultNavigation,
    navigateBack: () -> Unit,
    viewModel: CreateQRTextViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is CreateQRTextEvent.NavigateToScanResult -> {
                navigateToScanResult.navigate(event.qr, event.imagePath)
            }

            CreateQRTextEvent.NavigateBack -> navigateBack()
        }
    }

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
    val focusManager = LocalFocusManager.current

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
                        onClick = {
                            onAction(CreateQRTextAction.OnNavigateBackClick)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = stringResource(R.string.navigate_back)
                        )
                    }
                },
            )
        },
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = Modifier
            .pointerInput(Unit) {
                detectTapGestures {
                    focusManager.clearFocus()
                }
            }
    ) { innerPadding ->
        QRCraftQRForm(
            onSubmit = {
                focusManager.clearFocus()
                onAction(CreateQRTextAction.OnSubmitClick)
            },
            canSubmit = state.canSubmit,
            loading = state.isLoading,
            modifier = Modifier
                .padding(innerPadding)
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