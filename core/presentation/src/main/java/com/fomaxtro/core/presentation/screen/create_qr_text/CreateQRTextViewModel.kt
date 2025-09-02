package com.fomaxtro.core.presentation.screen.create_qr_text

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fomaxtro.core.domain.model.QR
import com.fomaxtro.core.domain.qr.QRParser
import com.fomaxtro.core.domain.util.ValidationResult
import com.fomaxtro.core.domain.validator.CreateQRTextValidator
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
    private val qrParser: QRParser
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
            val qr = QR.Text(state.value.text)

            eventChannel.send(
                CreateQRTextEvent.NavigateToScanResult(
                    qrParser.convertToString(qr)
                )
            )
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