package com.fomaxtro.core.presentation.screen.create_qr_phone_number

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fomaxtro.core.domain.util.ValidationResult
import com.fomaxtro.core.domain.validator.CreateQRPhoneNumberValidator
import com.fomaxtro.core.presentation.model.QR
import com.fomaxtro.core.presentation.service.QRImageService
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CreateQRPhoneNumberViewModel(
    private val validator: CreateQRPhoneNumberValidator,
    private val qrImageService: QRImageService
) : ViewModel() {
    private val _state = MutableStateFlow(CreateQRPhoneNumberState())
    val state = _state
        .onStart {
            observePhoneNumber()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            CreateQRPhoneNumberState()
        )

    private val eventChannel = Channel<CreateQRPhoneNumberEvent>()
    val events = eventChannel.receiveAsFlow()

    private fun observePhoneNumber() {
        state
            .map { it.phoneNumber }
            .distinctUntilChanged()
            .onEach { phoneNumber ->
                _state.update {
                    it.copy(
                        canSubmit = validator.validatePhoneNumber(phoneNumber)
                                is ValidationResult.Success
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun onAction(action: CreateQRPhoneNumberAction) {
        when (action) {
            CreateQRPhoneNumberAction.OnNavigateBackClick -> onNavigateBackClick()

            is CreateQRPhoneNumberAction.OnPhoneNumberChange -> {
                onPhoneNumberChange(action.phoneNumber)
            }

            CreateQRPhoneNumberAction.OnSubmitClick -> onSubmitClick()
        }
    }

    private fun onSubmitClick() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = true
                )
            }

            val qr = QR.PhoneNumber(state.value.phoneNumber)
            val imagePath = qrImageService.generateAndSaveQR(qr)

            _state.update {
                it.copy(
                    isLoading = false
                )
            }

            eventChannel.send(CreateQRPhoneNumberEvent.NavigateToScanResult(qr, imagePath))
        }
    }

    private fun onPhoneNumberChange(phoneNumber: String) {
        _state.update {
            it.copy(
                phoneNumber = phoneNumber
            )
        }
    }

    private fun onNavigateBackClick() {
        viewModelScope.launch {
            eventChannel.send(CreateQRPhoneNumberEvent.NavigateBack)
        }
    }
}