package com.fomaxtro.core.presentation.screen.create_qr_geolocation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fomaxtro.core.domain.model.QRCode
import com.fomaxtro.core.domain.model.QRCodeEntry
import com.fomaxtro.core.domain.model.QRCodeSource
import com.fomaxtro.core.domain.repository.QRCodeRepository
import com.fomaxtro.core.domain.util.Result
import com.fomaxtro.core.domain.util.ValidationResult
import com.fomaxtro.core.domain.validator.CreateQRGeolocationValidator
import com.fomaxtro.core.presentation.mapper.toUiText
import com.fomaxtro.core.presentation.util.InputValidator
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

class CreateQRGeolocationViewModel(
    private val validator: CreateQRGeolocationValidator,
    private val qrCodeRepository: QRCodeRepository
) : ViewModel() {
    private var firstLaunch = false

    private val _state = MutableStateFlow(CreateQRGeolocationState())
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
            CreateQRGeolocationState()
        )

    private val eventChannel = Channel<CreateQRGeolocationEvent>()
    val events = eventChannel.receiveAsFlow()

    private val latitudeFlow = state
        .map { it.latitude }
        .distinctUntilChanged()
        .map { latitude ->
            validator.validateLatitude(latitude) is ValidationResult.Success
        }
    private val longitudeFlow = state
        .map { it.longitude }
        .distinctUntilChanged()
        .map { longitude ->
            validator.validateLongitude(longitude) is ValidationResult.Success
        }

    private fun observeCanSubmit() {
        combine(latitudeFlow, longitudeFlow) { isValidLatitude, isValidLongitude ->
            isValidLatitude && isValidLongitude
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

    fun onAction(action: CreateQRGeolocationAction) {
        when (action) {
            is CreateQRGeolocationAction.OnLatitudeChanged -> onLatitudeChanged(action.latitude)
            is CreateQRGeolocationAction.OnLongitudeChanged -> onLongitudeChanged(action.longitude)
            CreateQRGeolocationAction.OnNavigateBackClick -> onNavigateBackClick()
            CreateQRGeolocationAction.OnSubmitClick -> onSubmitClick()
        }
    }

    private fun onSubmitClick() {
        viewModelScope.launch {
            val qrCode = with(state.value) {
                QRCode.Geolocation(latitude.toDouble(), longitude.toDouble())
            }
            val qrEntry = QRCodeEntry(
                title = null,
                qrCode = qrCode,
                source = QRCodeSource.GENERATED
            )

            when (val result = qrCodeRepository.save(qrEntry)) {
                is Result.Error -> {
                    eventChannel.send(
                        CreateQRGeolocationEvent.ShowSystemMessage(
                            result.error.toUiText()
                        )
                    )
                }

                is Result.Success -> {
                    eventChannel.send(
                        CreateQRGeolocationEvent.NavigateToScanResult(result.data)
                    )
                }
            }
        }
    }

    private fun onNavigateBackClick() {
        viewModelScope.launch {
            eventChannel.send(CreateQRGeolocationEvent.NavigateBack)
        }
    }

    private fun onLongitudeChanged(longitude: String) {
        if (!InputValidator.isValidInputNumber(longitude)) {
            return
        }

        _state.update {
            it.copy(
                longitude = longitude
            )
        }
    }

    private fun onLatitudeChanged(latitude: String) {
        if (!InputValidator.isValidInputNumber(latitude)) {
            return
        }

        _state.update {
            it.copy(
                latitude = latitude
            )
        }
    }
}