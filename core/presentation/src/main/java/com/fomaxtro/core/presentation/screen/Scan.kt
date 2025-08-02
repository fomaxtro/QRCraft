package com.fomaxtro.core.presentation.screen

import android.Manifest
import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.DialogProperties
import androidx.core.app.ActivityCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fomaxtro.core.presentation.R
import com.fomaxtro.core.presentation.designsystem.snackbars.QRCraftSnackbar
import com.fomaxtro.core.presentation.designsystem.theme.QRCraftTheme
import com.fomaxtro.core.presentation.designsystem.theme.SurfaceHigher
import com.fomaxtro.core.presentation.ui.ObserveAsEvents
import org.koin.androidx.compose.koinViewModel

@Composable
fun ScanRoot(
    onCloseApp: () -> Unit,
    onCameraPermissionDenied: () -> Unit,
    onAlwaysDeniedCameraPermission: () -> Unit,
    viewModel: ScanViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val snackbarHostState = remember {
        SnackbarHostState()
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        when {
            isGranted -> {
                viewModel.onAction(ScanAction.OnCameraPermissionGranted)
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                context as Activity,
                Manifest.permission.CAMERA
            ) -> {
                onCameraPermissionDenied()
            }

            else -> {
                onAlwaysDeniedCameraPermission()
            }
        }
    }

    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            ScanEvent.CloseApp -> onCloseApp()
            ScanEvent.RequestCameraPermission -> {
                launcher.launch(Manifest.permission.CAMERA)
            }

            is ScanEvent.ShowMessage -> {
                snackbarHostState.showSnackbar(
                    message = event.message.asString(context)
                )
            }
        }
    }

    ScanScreen(
        state = state,
        snackbarHostState = snackbarHostState,
        onAction = viewModel::onAction
    )
}

@Composable
private fun ScanScreen(
    onAction: (ScanAction) -> Unit = {},
    snackbarHostState: SnackbarHostState = SnackbarHostState(),
    state: ScanState
) {
    if (!state.hasCameraPermission) {
        AlertDialog(
            onDismissRequest = {},
            properties = DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false
            ),
            title = {
                Text(
                    text = stringResource(R.string.camera_required),
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            },
            text = {
                Text(
                    text = stringResource(R.string.camera_permission_description),
                    textAlign = TextAlign.Center
                )
            },
            dismissButton = {
                Button(
                    onClick = {
                        onAction(ScanAction.OnCloseAppClick)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SurfaceHigher,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    ),
                ) {
                    Text(
                        text = stringResource(R.string.close_app),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onAction(ScanAction.OnGrantAccessClick)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SurfaceHigher,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    )
                ) {
                    Text(
                        text = stringResource(R.string.grant_access)
                    )
                }
            },
            containerColor = MaterialTheme.colorScheme.surface
        )
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState) {
                QRCraftSnackbar(it)
            }
        }
    ) { innerPadding ->

    }
}

@Preview
@Composable
private fun ScanScreenPreview() {
    QRCraftTheme {
        val snackbarHostState = remember {
            SnackbarHostState()
        }

        LaunchedEffect(Unit) {
            snackbarHostState.showSnackbar(
                message = "Camera permission granted"
            )
        }

        ScanScreen(
            state = ScanState(
                hasCameraPermission = true
            ),
            snackbarHostState = snackbarHostState
        )
    }
}