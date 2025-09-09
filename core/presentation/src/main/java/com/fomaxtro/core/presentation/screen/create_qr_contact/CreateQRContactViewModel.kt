package com.fomaxtro.core.presentation.screen.create_qr_contact

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fomaxtro.core.domain.model.QRCode
import com.fomaxtro.core.domain.model.QRCodeEntry
import com.fomaxtro.core.domain.model.QRCodeSource
import com.fomaxtro.core.domain.repository.QRCodeRepository
import com.fomaxtro.core.domain.util.Result
import com.fomaxtro.core.domain.util.ValidationResult
import com.fomaxtro.core.domain.validator.CreateQRContactValidator
import com.fomaxtro.core.presentation.mapper.toUiText
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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CreateQRContactViewModel(
    private val validator: CreateQRContactValidator,
    private val qrCodeRepository: QRCodeRepository
) : ViewModel() {
    private var firstLaunch = false

    private val _state = MutableStateFlow(CreateQRContactState())
    val state = _state
        .onStart {
            if (!firstLaunch) {
                observeCanSubmit()

                firstLaunch = true
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            CreateQRContactState()
        )

    private val eventChannel = Channel<CreateQRContactEvent>()
    val events = eventChannel.receiveAsFlow()

    private val nameFlow = state
        .map { it.name }
        .distinctUntilChanged()
        .map { name ->
            validator.validateName(name) is ValidationResult.Success
        }
    private val emailFlow = state
        .map { it.email }
        .distinctUntilChanged()
        .map { email ->
            validator.validateEmail(email) is ValidationResult.Success
        }
    private val phoneNumberFlow = state
        .map { it.phoneNumber }
        .distinctUntilChanged()
        .map { phoneNumber ->
            validator.validatePhoneNumber(phoneNumber) is ValidationResult.Success
        }

    private fun observeCanSubmit() {
        combine(
            nameFlow,
            emailFlow,
            phoneNumberFlow
        ) { isValidName, isValidEmail, isValidPhoneNumber ->
            isValidName && isValidEmail && isValidPhoneNumber
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

    fun onAction(action: CreateQRContactAction) {
        when (action) {
            is CreateQRContactAction.OnEmailChange -> onEmailChange(action.email)
            is CreateQRContactAction.OnNameChange -> onNameChange(action.name)
            is CreateQRContactAction.OnPhoneNumberChange -> onPhoneNumberChange(action.phoneNumber)
            CreateQRContactAction.OnSubmitClick -> onSubmitClick()
            CreateQRContactAction.OnNavigateBackClick -> onNavigateBackClick()
        }
    }

    private fun onNavigateBackClick() {
        viewModelScope.launch {
            eventChannel.send(CreateQRContactEvent.NavigateBack)
        }
    }

    private fun onSubmitClick() {
        viewModelScope.launch {
            val qrCode = with(state.value) {
                QRCode.Contact(
                    name = name,
                    email = email,
                    phoneNumber = phoneNumber
                )
            }
            val qrEntry = QRCodeEntry(
                title = null,
                qrCode = qrCode,
                source = QRCodeSource.GENERATED
            )

            when (val result = qrCodeRepository.save(qrEntry)) {
                is Result.Error -> {
                    eventChannel.send(
                        CreateQRContactEvent.ShowSystemMessage(result.error.toUiText())
                    )
                }

                is Result.Success -> {
                    eventChannel.send(
                        CreateQRContactEvent.NavigateToScanResult(result.data)
                    )
                }
            }
        }
    }

    private fun onPhoneNumberChange(phoneNumber: String) {
        _state.update {
            it.copy(
                phoneNumber = phoneNumber
            )
        }
    }

    private fun onNameChange(name: String) {
        _state.update {
            it.copy(
                name = name
            )
        }
    }

    private fun onEmailChange(email: String) {
        _state.update {
            it.copy(
                email = email
            )
        }
    }
}