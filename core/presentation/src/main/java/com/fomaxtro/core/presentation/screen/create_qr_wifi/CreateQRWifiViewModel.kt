package com.fomaxtro.core.presentation.screen.create_qr_wifi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fomaxtro.core.domain.util.ValidationResult
import com.fomaxtro.core.domain.validator.CreateQRWifiValidator
import com.fomaxtro.core.presentation.model.QR
import com.fomaxtro.core.presentation.model.WifiEncryptionType
import com.fomaxtro.core.presentation.service.QRImageService
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CreateQRWifiViewModel(
    private val validator: CreateQRWifiValidator,
    private val qrImageService: QRImageService
) : ViewModel() {
    private val _state = MutableStateFlow(CreateQRWifiState())
    val state = _state
        .onStart {
            observeCanSubmit()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            CreateQRWifiState()
        )

    private val eventChannel = Channel<CreateQRWifiEvent>()
    val events = eventChannel.receiveAsFlow()

    private val ssidFlow = state
        .map { it.ssid }
        .distinctUntilChanged()
        .map { ssid ->
            validator.validateSsid(ssid) is ValidationResult.Success
        }
    private val passwordFlow = state
        .map { it.encryptionType to it.password }
        .distinctUntilChanged()
        .transform { (encryptionType, password) ->
            if (encryptionType == WifiEncryptionType.OPEN) {
                emit(true)
            } else {
                emit(validator.validatePassword(password) is ValidationResult.Success)
            }
        }
    private val encryptionTypeFlow = state
        .map { it.encryptionType }
        .distinctUntilChanged()
        .map { encryptionType ->
            validator.validateEncryptionType(encryptionType) is ValidationResult.Success
        }

    private fun observeCanSubmit() {
        combine(
            ssidFlow,
            passwordFlow,
            encryptionTypeFlow
        ) { isValidSsid, isValidPassword, isValidEncryptionType ->
            isValidSsid && isValidPassword && isValidEncryptionType
        }
            .onEach { canSubmit ->
                _state.update {
                    it.copy(
                        canSubmit = canSubmit
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun onAction(action: CreateQRWifiAction) {
        when (action) {
            is CreateQRWifiAction.OnEncryptionTypeSelected -> {
                onEncryptionTypeSelected(action.encryptionType)
            }

            CreateQRWifiAction.OnNavigateBackClick -> onNavigateBackClick()
            is CreateQRWifiAction.OnPasswordChange -> onPasswordChange(action.password)
            is CreateQRWifiAction.OnSSIDChange -> onSSIDChange(action.ssid)
            CreateQRWifiAction.OnSubmitClick -> onSubmitClick()
        }
    }

    private fun onSubmitClick() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = true
                )
            }

            val qr = with(state.value) {
                QR.Wifi(
                    ssid = ssid,
                    password = if (encryptionType == WifiEncryptionType.OPEN) "" else password,
                    encryptionType = encryptionType!!
                )
            }
            val imagePath = qrImageService.generateAndSaveQR(qr)

            _state.update {
                it.copy(
                    isLoading = false
                )
            }

            eventChannel.send(
                CreateQRWifiEvent.NavigateToScanResult(
                    qr = qr,
                    imagePath = imagePath
                )
            )
        }
    }

    private fun onSSIDChange(ssid: String) {
        _state.update {
            it.copy(
                ssid = ssid
            )
        }
    }

    private fun onPasswordChange(password: String) {
        _state.update {
            it.copy(
                password = password
            )
        }
    }

    private fun onNavigateBackClick() {
        viewModelScope.launch {
            eventChannel.send(CreateQRWifiEvent.NavigateBack)
        }
    }

    private fun onEncryptionTypeSelected(encryptionType: WifiEncryptionType) {
        _state.update {
            it.copy(
                encryptionType = encryptionType
            )
        }
    }
}