package com.fomaxtro.core.presentation.screen.create_qr_wifi

import android.widget.Toast
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fomaxtro.core.domain.model.WifiEncryptionType
import com.fomaxtro.core.presentation.R
import com.fomaxtro.core.presentation.designsystem.cards.QRCraftQRForm
import com.fomaxtro.core.presentation.designsystem.scaffolds.QRCraftScaffold
import com.fomaxtro.core.presentation.designsystem.text_fields.QRCraftOutlinedTextField
import com.fomaxtro.core.presentation.designsystem.text_fields.QRCraftOutlinedTextFieldDefaults
import com.fomaxtro.core.presentation.designsystem.theme.QRCraftTheme
import com.fomaxtro.core.presentation.ui.ObserveAsEvents
import com.fomaxtro.core.presentation.util.ScanResultNavigation
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CreateQRWifiRoot(
    navigateToScanResult: ScanResultNavigation,
    navigateBack: () -> Unit,
    viewModel: CreateQRWifiViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            CreateQRWifiEvent.NavigateBack -> navigateBack()

            is CreateQRWifiEvent.NavigateToScanResult -> {
                navigateToScanResult.navigate(event.id)
            }

            is CreateQRWifiEvent.ShowSystemMessage -> {
                Toast.makeText(
                    context,
                    event.message.asString(context),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    CreateQRWifiScreen(
        onAction = viewModel::onAction,
        state = state
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreateQRWifiScreen(
    onAction: (CreateQRWifiAction) -> Unit = {},
    state: CreateQRWifiState
) {
    val focusManager = LocalFocusManager.current
    var isExpanded by remember {
        mutableStateOf(false)
    }

    QRCraftScaffold(
        title = stringResource(R.string.wifi),
        onBackClick = {
            onAction(CreateQRWifiAction.OnNavigateBackClick)
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
                onAction(CreateQRWifiAction.OnSubmitClick)
            },
            modifier = Modifier
                .padding(innerPadding)
                .imePadding(),
            canSubmit = state.canSubmit
        ) {
            QRCraftOutlinedTextField(
                value = state.ssid,
                onValueChange = {
                    onAction(CreateQRWifiAction.OnSSIDChange(it))
                },
                modifier = Modifier
                    .fillMaxWidth(),
                placeholder = {
                    Text(stringResource(R.string.ssid))
                }
            )

            if (state.encryptionType != WifiEncryptionType.OPEN) {
                QRCraftOutlinedTextField(
                    value = state.password,
                    onValueChange = {
                        onAction(CreateQRWifiAction.OnPasswordChange(it))
                    },
                    modifier = Modifier
                        .fillMaxWidth(),
                    placeholder = {
                        Text(stringResource(R.string.password))
                    }
                )
            }

            ExposedDropdownMenuBox(
                expanded = isExpanded,
                onExpandedChange = { isExpanded = it }
            ) {
                QRCraftOutlinedTextField(
                    value = state.encryptionType?.toString() ?: "",
                    onValueChange = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(MenuAnchorType.PrimaryNotEditable),
                    placeholder = {
                        Text(stringResource(R.string.encryption_type))
                    },
                    colors = QRCraftOutlinedTextFieldDefaults.exposedDropdownMenuColors(),
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
                    },
                    readOnly = true,
                )

                ExposedDropdownMenu(
                    expanded = isExpanded,
                    onDismissRequest = { isExpanded = false },
                    shape = MaterialTheme.shapes.large,
                    containerColor = MaterialTheme.colorScheme.surface
                ) {
                    WifiEncryptionType.entries.forEach { encryptionType ->
                        DropdownMenuItem(
                            onClick = {
                                isExpanded = false
                                onAction(
                                    CreateQRWifiAction.OnEncryptionTypeSelected(encryptionType)
                                )
                            },
                            text = {
                                Text(encryptionType.name)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun CreateQRWifiScreenPreview() {
    QRCraftTheme {
        CreateQRWifiScreen(
            state = CreateQRWifiState(
                encryptionType = WifiEncryptionType.WPA
            )
        )
    }
}