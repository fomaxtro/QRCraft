package com.fomaxtro.core.presentation.screen.create_qr_contact

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
fun CreateQRContactRoot(
    navigateToScanResult: ScanResultNavigation,
    navigateBack: () -> Unit,
    viewModel: CreateQRContactViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            CreateQRContactEvent.NavigateBack -> navigateBack()

            is CreateQRContactEvent.NavigateToScanResult -> {
                navigateToScanResult.navigate(event.qr, event.imagePath)
            }
        }
    }

    CreateQRContactScreen(
        onAction = viewModel::onAction,
        state = state
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreateQRContactScreen(
    onAction: (CreateQRContactAction) -> Unit = {},
    state: CreateQRContactState
) {
    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.contact),
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onAction(CreateQRContactAction.OnNavigateBackClick)
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
                onAction(CreateQRContactAction.OnSubmitClick)
            },
            modifier = Modifier
                .padding(innerPadding)
                .imePadding(),
            canSubmit = state.canSubmit,
            loading = state.isLoading
        ) {
            QRCraftOutlinedTextField(
                value = state.name,
                onValueChange = {
                    onAction(CreateQRContactAction.OnNameChange(it))
                },
                modifier = Modifier
                    .fillMaxWidth(),
                placeholder = {
                    Text(stringResource(R.string.name))
                }
            )

            QRCraftOutlinedTextField(
                value = state.email,
                onValueChange = {
                    onAction(CreateQRContactAction.OnEmailChange(it))
                },
                modifier = Modifier
                    .fillMaxWidth(),
                placeholder = {
                    Text(stringResource(R.string.email))
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email
                )
            )

            QRCraftOutlinedTextField(
                value = state.phoneNumber,
                onValueChange = {
                    onAction(CreateQRContactAction.OnPhoneNumberChange(it))
                },
                modifier = Modifier
                    .fillMaxWidth(),
                placeholder = {
                    Text(stringResource(R.string.phone_number))
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone
                )
            )
        }
    }
}

@Preview
@Composable
private fun CreateQRContactScreenPreview() {
    QRCraftTheme {
        CreateQRContactScreen(
            state = CreateQRContactState()
        )
    }
}