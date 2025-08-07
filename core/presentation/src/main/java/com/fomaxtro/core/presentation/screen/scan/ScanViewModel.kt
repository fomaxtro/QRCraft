package com.fomaxtro.core.presentation.screen.scan

import android.Manifest
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fomaxtro.core.domain.PermissionChecker
import com.fomaxtro.core.presentation.R
import com.fomaxtro.core.presentation.model.QR
import com.fomaxtro.core.presentation.ui.UiText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

class ScanViewModel(
    permissionChecker: PermissionChecker
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
            is ScanAction.OnQrScanned -> onQrScanned(action.qr)
        }
    }

    private fun onQrScanned(qr: QR) {
        _state.update {
            it.copy(
                isProcessingQr = true
            )
        }

        Timber.tag("ScanViewModel").d("onQrScanned: $qr")
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