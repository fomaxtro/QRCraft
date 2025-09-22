package com.fomaxtro.core.presentation.screen.scan

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.widget.Toast
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledIconToggleButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fomaxtro.core.presentation.R
import com.fomaxtro.core.presentation.designsystem.snackbars.QRCraftSnackbar
import com.fomaxtro.core.presentation.designsystem.theme.QRCraftIcons
import com.fomaxtro.core.presentation.designsystem.theme.QRCraftTheme
import com.fomaxtro.core.presentation.designsystem.theme.surfaceHigher
import com.fomaxtro.core.presentation.qr.QrAnalyzer
import com.fomaxtro.core.presentation.screen.scan.components.CameraPreview
import com.fomaxtro.core.presentation.screen.scan.components.ErrorDialog
import com.fomaxtro.core.presentation.screen.scan.components.OverlayLoading
import com.fomaxtro.core.presentation.screen.scan.components.QRScanOverlay
import com.fomaxtro.core.presentation.ui.ObserveAsEvents
import com.fomaxtro.core.presentation.util.ScanResultNavigation
import org.koin.androidx.compose.koinViewModel
import kotlin.math.roundToInt

@SuppressLint("SourceLockedOrientationActivity")
@Composable
fun ScanRoot(
    onCloseApp: () -> Unit,
    onCameraPermissionDenied: () -> Unit,
    onAlwaysDeniedCameraPermission: () -> Unit,
    navigateToScanResult: ScanResultNavigation,
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
                QrAnalyzer(
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
            if (lifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.CREATED)) {
                viewModel.onAction(ScanAction.OnFlashToggle(false))
                activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
                cameraController.unbind()
            }
        }
    }

    LaunchedEffect(state.isProcessingQr) {
        if (state.isProcessingQr) {
            cameraController.setEnabledUseCases(0)
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
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

    val pickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let {
            viewModel.onAction(ScanAction.OnImagePicked(it))
        }
    }

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            ScanEvent.CloseApp -> onCloseApp()

            ScanEvent.RequestCameraPermission -> {
                permissionLauncher.launch(Manifest.permission.CAMERA)
            }

            is ScanEvent.ShowMessage -> {
                snackbarHostState.showSnackbar(
                    message = event.message.asString(context)
                )
            }

            is ScanEvent.NavigateToScanResult -> {
                navigateToScanResult.navigate(event.id)
            }

            is ScanEvent.ShowSystemMessage -> {
                Toast.makeText(
                    context,
                    event.message.asString(context),
                    Toast.LENGTH_LONG
                ).show()
            }

            is ScanEvent.ToggleFlash -> {
                cameraController.enableTorch(event.isFlashActive)
            }

            ScanEvent.OpenGallery -> {
                pickerLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            }
        }
    }

    val isFlashSupported by produceState(false) {
        cameraController.initializationFuture.addListener({
            value = cameraController.cameraInfo?.hasFlashUnit() ?: false
        }, ContextCompat.getMainExecutor(context))

        awaitDispose {
            cameraController.initializationFuture.cancel(true)
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
        frameSize = frameSize,
        isFlashSupported = isFlashSupported
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
private fun ScanScreen(
    state: ScanState,
    frameSize: Dp,
    cameraPreview: @Composable () -> Unit = {},
    onAction: (ScanAction) -> Unit = {},
    snackbarHostState: SnackbarHostState = SnackbarHostState(),
    isFlashSupported: Boolean = false
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

    if (state.showQrNotFoundDialog) {
        ErrorDialog(
            onDismissRequest = {
                onAction(ScanAction.OnQrNotFoundDialogDismiss)
            },
            text = stringResource(R.string.qr_not_found)
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

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .padding(horizontal = 24.dp)
                    .padding(top = 19.dp)
                    .statusBarsPadding(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                FilledIconToggleButton(
                    checked = state.isFlashActive,
                    onCheckedChange = {
                        onAction(ScanAction.OnFlashToggle(it))
                    },
                    colors = IconButtonDefaults.iconToggleButtonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceHigher,
                        checkedContainerColor = MaterialTheme.colorScheme.primary,
                        checkedContentColor = MaterialTheme.colorScheme.onSurface,
                        disabledContainerColor = MaterialTheme.colorScheme.surfaceHigher
                    ),
                    enabled = isFlashSupported
                ) {
                    Icon(
                        imageVector = if (state.isFlashActive) {
                            QRCraftIcons.Default.ZapOff
                        } else {
                            QRCraftIcons.Default.Zap
                        },
                        contentDescription = if (state.isFlashActive) {
                            stringResource(R.string.flash_off)
                        } else {
                            stringResource(R.string.flash_on)
                        },
                        modifier = Modifier.size(16.dp)
                    )
                }

                FilledIconButton(
                    onClick = {
                        onAction(ScanAction.OnOpenGalleryClick)
                    },
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceHigher
                    )
                ) {
                    Icon(
                        imageVector = QRCraftIcons.Default.Image,
                        contentDescription = stringResource(R.string.open_gallery),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

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
                isProcessingQr = false,
                isFlashActive = true
            ),
            snackbarHostState = snackbarHostState,
            frameSize = 324.dp
        )
    }
}