package com.fomaxtro.core.presentation.screen.scan

import android.Manifest
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fomaxtro.core.domain.FileManager
import com.fomaxtro.core.domain.PermissionChecker
import com.fomaxtro.core.presentation.R
import com.fomaxtro.core.presentation.model.QRScanResult
import com.fomaxtro.core.presentation.ui.UiText
import com.fomaxtro.core.presentation.util.toByteArray
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ScanViewModel(
    permissionChecker: PermissionChecker,
    private val fileManager: FileManager
) : ViewModel() {
    private val _state = MutableStateFlow(
        ScanState(
            hasCameraPermission = permissionChecker
                .hasPermission(Manifest.permission.CAMERA)
        )
    )
    val state = _state.asStateFlow()

    private val eventChannel = Channel<ScanEvent>()
    val events = eventChannel.receiveAsFlow()

    fun onAction(action: ScanAction) {
        when (action) {
            is ScanAction.OnCameraPermissionGranted -> onCameraPermissionGranted()
            ScanAction.OnCloseAppClick -> onCloseAppClick()
            ScanAction.OnGrantAccessClick -> onGrantAccessClick()
            is ScanAction.OnQrScanned -> onQrScanned(action.qrScanResult)
        }
    }

    private fun onQrScanned(qrScanResult: QRScanResult) {
        _state.update {
            it.copy(
                isProcessingQr = true
            )
        }

        viewModelScope.launch {
            val imagePath = fileManager.saveImage(qrScanResult.image.toByteArray(90))

            eventChannel.send(
                ScanEvent.NavigateToScanResult(
                    qr = qrScanResult.qr,
                    imagePath = imagePath
                )
            )
        }
    }

    private fun onGrantAccessClick() {
        viewModelScope.launch {
            eventChannel.send(ScanEvent.RequestCameraPermission)
        }
    }

    private fun onCloseAppClick() {
        viewModelScope.launch {
            eventChannel.send(ScanEvent.CloseApp)
        }
    }

    private fun onCameraPermissionGranted() {
        _state.update {
            it.copy(
                hasCameraPermission = true
            )
        }

        viewModelScope.launch {
            eventChannel.send(
                ScanEvent.ShowSnackbar(
                    message = UiText.StringResource(R.string.camera_permission_granted)
                )
            )
        }
    }
}