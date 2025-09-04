package com.fomaxtro.core.presentation.screen.scan

import android.Manifest
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fomaxtro.core.domain.PermissionChecker
import com.fomaxtro.core.domain.model.CreateQRCodeRequest
import com.fomaxtro.core.domain.model.QRCode
import com.fomaxtro.core.domain.model.QRCodeSource
import com.fomaxtro.core.domain.qr.QRParser
import com.fomaxtro.core.domain.repository.QRCodeRepository
import com.fomaxtro.core.presentation.R
import com.fomaxtro.core.presentation.mapper.toQRCode
import com.fomaxtro.core.presentation.ui.UiText
import com.google.mlkit.vision.barcode.common.Barcode
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.reflect.KClass

class ScanViewModel(
    permissionChecker: PermissionChecker,
    private val qrParser: QRParser,
    private val qrCodeRepository: QRCodeRepository,
    private val defaultTitles: Map<KClass<out QRCode>, String>
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
            is ScanAction.OnQrScanned -> onQrScanned(action.barcode)
        }
    }

    private fun onQrScanned(barcode: Barcode) {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isProcessingQr = true
                )
            }

            val qrCode = barcode.toQRCode()

            qrCodeRepository.save(
                CreateQRCodeRequest(
                    title = defaultTitles[qrCode::class] ?: "",
                    qrCode = qrCode,
                    source = QRCodeSource.SCANNED
                )
            )

            eventChannel.send(
                ScanEvent.NavigateToScanResult(
                    qrParser.convertToString(qrCode)
                )
            )

            _state.update {
                it.copy(
                    isProcessingQr = false
                )
            }
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