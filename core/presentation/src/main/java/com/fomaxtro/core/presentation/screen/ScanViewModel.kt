package com.fomaxtro.core.presentation.screen

import android.Manifest
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fomaxtro.core.domain.PermissionChecker
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

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
            is ScanAction.OnCameraPermissionGranted -> onCameraPermissionGranted(action.isGranted)
        }
    }

    private fun onCameraPermissionGranted(granted: Boolean) {
        _state.update {
            it.copy(
                hasCameraPermission = granted
            )
        }

        if (!granted) {
            viewModelScope.launch {
                eventChannel.send(ScanEvent.CameraPermissionDenied)
            }
        }
    }
}