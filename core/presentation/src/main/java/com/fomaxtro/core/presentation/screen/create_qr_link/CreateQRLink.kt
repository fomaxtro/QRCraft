package com.fomaxtro.core.presentation.screen.create_qr_link

import android.widget.Toast
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fomaxtro.core.presentation.R
import com.fomaxtro.core.presentation.designsystem.cards.QRCraftQRForm
import com.fomaxtro.core.presentation.designsystem.scaffolds.QRCraftScaffold
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
    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            CreateQRLinkEvent.NavigateBack -> navigateBack()

            is CreateQRLinkEvent.NavigateToScanResult -> {
                navigateToScanResult.navigate(event.id)
            }

            is CreateQRLinkEvent.ShowSystemMessage -> {
                Toast.makeText(
                    context,
                    event.message.asString(context),
                    Toast.LENGTH_LONG
                ).show()
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

    QRCraftScaffold(
        title = stringResource(R.string.link),
        onBackClick = {
            onAction(CreateQRLinkAction.OnNavigateBackClick)
        },
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
            canSubmit = state.canSubmit
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