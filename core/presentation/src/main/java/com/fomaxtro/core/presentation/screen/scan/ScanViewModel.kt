package com.fomaxtro.core.presentation.screen.scan

import android.Manifest
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fomaxtro.core.domain.PermissionChecker
import com.fomaxtro.core.domain.model.QRCodeEntry
import com.fomaxtro.core.domain.model.QRCodeSource
import com.fomaxtro.core.domain.repository.QRCodeRepository
import com.fomaxtro.core.domain.util.Result
import com.fomaxtro.core.presentation.R
import com.fomaxtro.core.presentation.mapper.toQRCode
import com.fomaxtro.core.presentation.mapper.toUiText
import com.fomaxtro.core.presentation.qr.QRDetector
import com.fomaxtro.core.presentation.ui.UiText
import com.google.mlkit.vision.barcode.common.Barcode
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ScanViewModel(
    permissionChecker: PermissionChecker,
    private val qrCodeRepository: QRCodeRepository,
    private val qrDetector: QRDetector
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
            is ScanAction.OnFlashToggle -> onFlashToggle(action.isFlashActive)
            ScanAction.OnOpenGalleryClick -> onOpenGalleryClick()
            is ScanAction.OnImagePicked -> onImagePicked(action.image)
            ScanAction.OnQrNotFoundDialogDismiss -> onQrNotFoundDialogDismiss()
        }
    }

    private fun onQrNotFoundDialogDismiss() {
        _state.update {
            it.copy(
                showQrNotFoundDialog = false
            )
        }
    }

    private fun onImagePicked(image: Uri) {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isProcessingQr = true
                )
            }

            val barcode = qrDetector.detect(image)

            _state.update {
                it.copy(
                    isProcessingQr = false
                )
            }

            if (barcode != null) {
                onQrScanned(barcode)
            } else {
                _state.update {
                    it.copy(
                        showQrNotFoundDialog = true
                    )
                }
            }
        }
    }

    private fun onOpenGalleryClick() {
        viewModelScope.launch {
            eventChannel.send(ScanEvent.OpenGallery)
        }
    }

    private fun onFlashToggle(flashActive: Boolean) {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isFlashActive = flashActive
                )
            }

            eventChannel.send(ScanEvent.ToggleFlash(flashActive))
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

            val result = qrCodeRepository.save(
                QRCodeEntry(
                    title = null,
                    qrCode = qrCode,
                    source = QRCodeSource.SCANNED
                )
            )

            when (result) {
                is Result.Error -> {
                    eventChannel.send(
                        ScanEvent.ShowSystemMessage(result.error.toUiText())
                    )
                }

                is Result.Success -> {
                    eventChannel.send(
                        ScanEvent.NavigateToScanResult(result.data)
                    )
                }
            }

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
                ScanEvent.ShowMessage(
                    message = UiText.StringResource(R.string.camera_permission_granted)
                )
            )
        }
    }
}