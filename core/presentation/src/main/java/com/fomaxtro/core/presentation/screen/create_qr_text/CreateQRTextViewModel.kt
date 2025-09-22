package com.fomaxtro.core.presentation.screen.create_qr_text

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fomaxtro.core.domain.model.QrCode
import com.fomaxtro.core.domain.model.QrCodeEntry
import com.fomaxtro.core.domain.model.QrCodeSource
import com.fomaxtro.core.domain.repository.QrCodeRepository
import com.fomaxtro.core.domain.util.Result
import com.fomaxtro.core.domain.util.ValidationResult
import com.fomaxtro.core.domain.validator.CreateQRTextValidator
import com.fomaxtro.core.presentation.mapper.toUiText
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

class CreateQRTextViewModel(
    private val validator: CreateQRTextValidator,
    private val qrCodeRepository: QrCodeRepository
) : ViewModel() {
    private var firstLaunch = false

    private val _state = MutableStateFlow(CreateQRTextState())
    val state = _state
        .onStart {
            if (!firstLaunch) {
                observeText()

                firstLaunch = true
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            CreateQRTextState()
        )

    private val eventChannel = Channel<CreateQRTextEvent>()
    val events = eventChannel.receiveAsFlow()

    private fun observeText() {
        state
            .map { it.text }
            .distinctUntilChanged()
            .onEach { text ->
                _state.update {
                    it.copy(
                        canSubmit = validator.validateText(text) is ValidationResult.Success
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun onAction(action: CreateQRTextAction) {
        when (action) {
            CreateQRTextAction.OnSubmitClick -> onSubmitClick()
            is CreateQRTextAction.OnTextChange -> onTextChange(action.text)
            CreateQRTextAction.OnNavigateBackClick -> onNavigateBackClick()
        }
    }

    private fun onNavigateBackClick() {
        viewModelScope.launch {
            eventChannel.send(CreateQRTextEvent.NavigateBack)
        }
    }

    private fun onSubmitClick() {
        viewModelScope.launch {
            val qrCode = QrCodeEntry(
                title = null,
                qrCode = QrCode.Text(state.value.text),
                source = QrCodeSource.GENERATED
            )

            when (val result = qrCodeRepository.save(qrCode)) {
                is Result.Error -> {
                    eventChannel.send(
                        CreateQRTextEvent.ShowSystemMessage(result.error.toUiText())
                    )
                }

                is Result.Success -> {
                    eventChannel.send(
                        CreateQRTextEvent.NavigateToScanResult(result.data)
                    )
                }
            }
        }
    }

    private fun onTextChange(text: String) {
        _state.update {
            it.copy(
                text = text
            )
        }
    }
}