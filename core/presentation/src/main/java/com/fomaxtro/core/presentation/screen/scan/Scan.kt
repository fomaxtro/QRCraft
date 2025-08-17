package com.fomaxtro.core.presentation.screen.scan

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fomaxtro.core.presentation.R
import com.fomaxtro.core.presentation.camera.CameraPreview
import com.fomaxtro.core.presentation.camera.QRAnalyzer
import com.fomaxtro.core.presentation.designsystem.snackbars.QRCraftSnackbar
import com.fomaxtro.core.presentation.designsystem.theme.QRCraftTheme
import com.fomaxtro.core.presentation.designsystem.theme.surfaceHigher
import com.fomaxtro.core.presentation.model.QR
import com.fomaxtro.core.presentation.screen.scan.components.OverlayLoading
import com.fomaxtro.core.presentation.screen.scan.components.QRScanOverlay
import com.fomaxtro.core.presentation.ui.ObserveAsEvents
import org.koin.androidx.compose.koinViewModel
import kotlin.math.roundToInt

@SuppressLint("SourceLockedOrientationActivity")
@Composable
fun ScanRoot(
    onCloseApp: () -> Unit,
    onCameraPermissionDenied: () -> Unit,
    onAlwaysDeniedCameraPermission: () -> Unit,
    navigateToScanResult: (qr: QR, imagePath: String) -> Unit,
    viewModel: ScanViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val activity = LocalActivity.current
    val density = LocalDensity.current
    val windowInfo = LocalWindowInfo.current

    val snackbarHostState = remember {
        SnackbarHostState()
    }
    val frameSize = 256.dp
    val frameSizePx = with(density) { frameSize.toPx() }

    val windowWidth = windowInfo.containerSize.width
    val windowHeight = windowInfo.containerSize.height
    val lifecycleOwner = LocalLifecycleOwner.current

    val cameraController = remember {
        LifecycleCameraController(context).apply {
            cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            imageAnalysisBackpressureStrategy = ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST

            setImageAnalysisAnalyzer(
                ContextCompat.getMainExecutor(context),
                QRAnalyzer(
                    frameSize = frameSizePx.roundToInt(),
                    windowWidth = windowWidth,
                    windowHeight = windowHeight,
                    onResult = { qrScanResult ->
                        viewModel.onAction(ScanAction.OnQrScanned(qrScanResult))
                    }
                )
            )
        }
    }

    DisposableEffect(Unit) {
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        cameraController.setEnabledUseCases(CameraController.IMAGE_ANALYSIS)
        cameraController.bindToLifecycle(lifecycleOwner)

        onDispose {
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            cameraController.unbind()
        }
    }

    LaunchedEffect(state.isProcessingQr) {
        if (state.isProcessingQr) {
            cameraController.setEnabledUseCases(0)
        }
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        when {
            isGranted -> {
                viewModel.onAction(ScanAction.OnCameraPermissionGranted)
            }

            activity != null && ActivityCompat.shouldShowRequestPermissionRationale(
                activity,
                Manifest.permission.CAMERA
            ) -> {
                onCameraPermissionDenied()
            }

            else -> {
                onAlwaysDeniedCameraPermission()
            }
        }
    }

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            ScanEvent.CloseApp -> onCloseApp()
            ScanEvent.RequestCameraPermission -> {
                launcher.launch(Manifest.permission.CAMERA)
            }

            is ScanEvent.ShowSnackbar -> {
                snackbarHostState.showSnackbar(
                    message = event.message.asString(context)
                )
            }

            is ScanEvent.NavigateToScanResult -> {
                navigateToScanResult(event.qr, event.imagePath)
            }
        }
    }

    ScanScreen(
        state = state,
        snackbarHostState = snackbarHostState,
        onAction = viewModel::onAction,
        cameraPreview = {
            CameraPreview(
                controller = cameraController,
                modifier = Modifier
                    .fillMaxSize()
            )
        },
        frameSize = frameSize
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
private fun ScanScreen(
    state: ScanState,
    frameSize: Dp,
    cameraPreview: @Composable () -> Unit = {},
    onAction: (ScanAction) -> Unit = {},
    snackbarHostState: SnackbarHostState = SnackbarHostState()
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
                        containerColor = MaterialTheme.colorScheme.surfaceHigher,
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
                        containerColor = MaterialTheme.colorScheme.surfaceHigher,
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
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            cameraPreview()

            QRScanOverlay(
                frameSize = frameSize,
                color = MaterialTheme.colorScheme.primary,
                placeHolder = stringResource(R.string.qr_scan_placeholder)
            )

            if (state.isProcessingQr) {
                OverlayLoading()
            }
        }
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
                hasCameraPermission = true,
                isProcessingQr = false
            ),
            snackbarHostState = snackbarHostState,
            frameSize = 324.dp
        )
    }
}