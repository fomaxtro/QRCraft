package com.fomaxtro.core.presentation.screen

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fomaxtro.core.presentation.R
import com.fomaxtro.core.presentation.designsystem.dialogs.QRCraftAlertDialog
import com.fomaxtro.core.presentation.designsystem.theme.QRCraftTheme
import com.fomaxtro.core.presentation.designsystem.theme.SurfaceHigher
import com.fomaxtro.core.presentation.ui.ObserveAsEvents
import org.koin.androidx.compose.koinViewModel

@Composable
fun ScanRoot(
    onCameraPermissionDenied: () -> Unit,
    viewModel: ScanViewModel = koinViewModel()
) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        viewModel.onAction(ScanAction.OnCameraPermissionGranted(isGranted))
    }

    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            ScanEvent.CameraPermissionDenied -> onCameraPermissionDenied()
        }
    }

    ScanScreen(
        state = state
    )
}

@Composable
private fun ScanScreen(
    state: ScanState
) {
    if (!state.hasCameraPermission) {
        QRCraftAlertDialog(
            onDismissRequest = {},
            title = stringResource(R.string.camera_required),
            text = stringResource(R.string.camera_permission_description),
            dismissButton = {
                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SurfaceHigher,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = stringResource(R.string.close_app),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SurfaceHigher,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = stringResource(R.string.grant_access)
                    )
                }
            }
        )
    }
}

@Preview
@Composable
private fun ScanScreenPreview() {
    QRCraftTheme {
        ScanScreen(
            state = ScanState()
        )
    }
}