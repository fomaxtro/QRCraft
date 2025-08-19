package com.fomaxtro.core.presentation.screen.create_qr_link

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
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
fun CreateQRLinkRoot(
    navigateToScanResult: ScanResultNavigation,
    navigateBack: () -> Unit,
    viewModel: CreateQRLinkViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            CreateQRLinkEvent.NavigateBack -> navigateBack()
            is CreateQRLinkEvent.NavigateToScanResult -> {
                navigateToScanResult.navigate(event.qr, event.imagePath)
            }
        }
    }

    CreateQRLinkScreen(
        onAction = viewModel::onAction,
        state = state
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreateQRLinkScreen(
    onAction: (CreateQRLinkAction) -> Unit = {},
    state: CreateQRLinkState
) {
    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.link),
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onAction(CreateQRLinkAction.OnNavigateBackClick)
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
                onAction(CreateQRLinkAction.OnSubmitClick)
            },
            modifier = Modifier
                .padding(innerPadding)
                .imePadding(),
            canSubmit = state.canSubmit,
            loading = state.isLoading
        ) {
            QRCraftOutlinedTextField(
                value = state.url,
                onValueChange = {
                    onAction(CreateQRLinkAction.OnUrlChange(it))
                },
                modifier = Modifier
                    .fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Uri
                ),
                placeholder = {
                    Text(stringResource(R.string.url))
                }
            )
        }
    }
}

@Preview
@Composable
private fun CreateQRLinkScreenPreview() {
    QRCraftTheme {
        CreateQRLinkScreen(
            state = CreateQRLinkState()
        )
    }
}