package com.fomaxtro.core.presentation.screen.create_qr_geolocation

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
fun CreateQRGeolocationRoot(
    navigateToScanResult: ScanResultNavigation,
    navigateBack: () -> Unit,
    viewModel: CreateQRGeolocationViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            CreateQRGeolocationEvent.NavigateBack -> navigateBack()

            is CreateQRGeolocationEvent.NavigateToScanResult -> {
                navigateToScanResult.navigate(event.id)
            }

            is CreateQRGeolocationEvent.ShowSystemMessage -> {
                Toast.makeText(
                    context,
                    event.message.asString(context),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    CreateQRGeolocationScreen(
        onAction = viewModel::onAction,
        state = state
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreateQRGeolocationScreen(
    onAction: (CreateQRGeolocationAction) -> Unit = {},
    state: CreateQRGeolocationState
) {
    val focusManager = LocalFocusManager.current

    QRCraftScaffold(
        title = stringResource(R.string.geolocation),
        onBackClick = {
            onAction(CreateQRGeolocationAction.OnNavigateBackClick)
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
                onAction(CreateQRGeolocationAction.OnSubmitClick)
            },
            modifier = Modifier
                .padding(innerPadding)
                .imePadding(),
            canSubmit = state.canSubmit,
            loading = state.isSubmitting
        ) {
            QRCraftOutlinedTextField(
                value = state.latitude,
                onValueChange = {
                    onAction(CreateQRGeolocationAction.OnLatitudeChanged(it))
                },
                modifier = Modifier
                    .fillMaxWidth(),
                placeholder = {
                    Text(stringResource(R.string.latitude))
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                )
            )

            QRCraftOutlinedTextField(
                value = state.longitude,
                onValueChange = {
                    onAction(CreateQRGeolocationAction.OnLongitudeChanged(it))
                },
                modifier = Modifier
                    .fillMaxWidth(),
                placeholder = {
                    Text(stringResource(R.string.longitude))
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                )
            )
        }
    }
}

@Preview
@Composable
private fun CreateQRGeolocationScreenPreview() {
    QRCraftTheme {
        CreateQRGeolocationScreen(
            state = CreateQRGeolocationState()
        )
    }
}