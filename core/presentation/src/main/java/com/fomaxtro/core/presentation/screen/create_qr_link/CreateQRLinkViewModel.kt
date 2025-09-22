package com.fomaxtro.core.presentation.screen.create_qr_link

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fomaxtro.core.domain.model.QrCode
import com.fomaxtro.core.domain.model.QrCodeEntry
import com.fomaxtro.core.domain.model.QrCodeSource
import com.fomaxtro.core.domain.repository.QrCodeRepository
import com.fomaxtro.core.domain.util.Result
import com.fomaxtro.core.domain.util.ValidationResult
import com.fomaxtro.core.domain.validator.CreateQRLinkValidator
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

class CreateQRLinkViewModel(
    private val validator: CreateQRLinkValidator,
    private val qrCodeRepository: QrCodeRepository
) : ViewModel() {
    private var firstLaunch = false

    private val _state = MutableStateFlow(CreateQRLinkState())
    val state = _state
        .onStart {
            if (!firstLaunch) {
                observeUrl()

                firstLaunch = true
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            CreateQRLinkState()
        )

    private val eventChannel = Channel<CreateQRLinkEvent>()
    val events = eventChannel.receiveAsFlow()

    private fun observeUrl() {
        state
            .map { it.url }
            .distinctUntilChanged()
            .onEach { url ->
                _state.update {
                    it.copy(
                        canSubmit = validator.validateUrl(url) is ValidationResult.Success
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun onAction(action: CreateQRLinkAction) {
        when (action) {
            CreateQRLinkAction.OnNavigateBackClick -> onNavigateBackClick()
            CreateQRLinkAction.OnSubmitClick -> onSubmitClick()
            is CreateQRLinkAction.OnUrlChange -> onUrlChange(action.url)
        }
    }

    private fun onUrlChange(url: String) {
        _state.update {
            it.copy(
                url = url
            )
        }
    }

    private fun onSubmitClick() {
        viewModelScope.launch {
            val qrEntry = QrCodeEntry(
                title = null,
                qrCode = QrCode.Link(state.value.url),
                source = QrCodeSource.GENERATED
            )

            when (val result = qrCodeRepository.save(qrEntry)) {
                is Result.Error -> {
                    eventChannel.send(
                        CreateQRLinkEvent.ShowSystemMessage(
                            result.error.toUiText()
                        )
                    )
                }

                is Result.Success -> {
                    eventChannel.send(
                        CreateQRLinkEvent.NavigateToScanResult(result.data)
                    )
                }
            }
        }
    }

    private fun onNavigateBackClick() {
        viewModelScope.launch {
            eventChannel.send(CreateQRLinkEvent.NavigateBack)
        }
    }
}