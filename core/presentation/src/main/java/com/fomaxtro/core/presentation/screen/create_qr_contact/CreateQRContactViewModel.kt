package com.fomaxtro.core.presentation.screen.create_qr_contact

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fomaxtro.core.domain.model.QR
import com.fomaxtro.core.domain.qr.QRParser
import com.fomaxtro.core.domain.util.ValidationResult
import com.fomaxtro.core.domain.validator.CreateQRContactValidator
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
    private val qrParser: QRParser
) : ViewModel() {
    private val _state = MutableStateFlow(CreateQRContactState())
    val state = _state
        .onStart {
            observeCanSubmit()
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
            val qr = with(state.value) {
                QR.Contact(
                    name = name,
                    email = email,
                    phoneNumber = phoneNumber
                )
            }

            eventChannel.send(
                CreateQRContactEvent.NavigateToScanResult(
                    qrParser.convertToString(qr)
                )
            )
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