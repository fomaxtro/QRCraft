package com.fomaxtro.core.presentation.screen.scan_result

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fomaxtro.core.domain.model.QrCodeEntry
import com.fomaxtro.core.domain.qr.QrParser
import com.fomaxtro.core.domain.repository.QrCodeRepository
import com.fomaxtro.core.domain.util.Result
import com.fomaxtro.core.domain.validator.ScanResultValidator
import com.fomaxtro.core.presentation.mapper.toFormattedUiText
import com.fomaxtro.core.presentation.mapper.toUiText
import com.fomaxtro.core.presentation.qr.QrGenerator
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ScanResultViewModel(
    id: Long,
    private val qrParser: QrParser,
    private val qrCodeRepository: QrCodeRepository,
    private val validator: ScanResultValidator
) : ViewModel() {
    private var firstLaunch = false
    private var qrEntry: QrCodeEntry? = null

    private val _state = MutableStateFlow(ScanResultState())
    val state = _state
        .onStart {
            if (!firstLaunch) {
                loadQR(id)
                observeTitle()

                firstLaunch = true
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000L),
            ScanResultState()
        )
    val titleState = TextFieldState()

    private val eventChannel = Channel<ScanResultEvent>()
    val events = eventChannel.receiveAsFlow()

    private suspend fun loadQR(id: Long) {
        when (val entry = qrCodeRepository.findById(id)) {
            is Result.Error -> {
                eventChannel.send(
                    ScanResultEvent.ShowSystemMessage(entry.error.toUiText())
                )
            }

            is Result.Success -> {
                qrEntry = entry.data

                val qrImage = QrGenerator.generate(
                    qrParser.convertToString(entry.data.qrCode)
                )

                titleState.setTextAndPlaceCursorAtEnd(entry.data.title ?: "")
                _state.update {
                    it.copy(
                        qr = entry.data.qrCode,
                        qrImage = qrImage.asImageBitmap(),
                        isLoading = false
                    )
                }
            }
        }
    }

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    private fun observeTitle() {
        snapshotFlow { titleState.text.toString() }
            .transformLatest { title ->
                if (validator.isValidTitleLength(title)) {
                    emit(title)
                } else {
                    titleState.setTextAndPlaceCursorAtEnd(title.dropLast(1))
                }
            }
            .debounce(500L)
            .distinctUntilChanged()
            .onEach { title -> updateQRTitle(title) }
            .launchIn(viewModelScope)
    }

    private suspend fun updateQRTitle(title: String) {
        qrEntry?.let { entry ->
            qrCodeRepository.save(
                entry.copy(
                    title = title.ifEmpty { null }
                )
            )
        }
    }

    fun onAction(action: ScanResultAction) {
        when (action) {
            ScanResultAction.OnNavigateBackClick -> onNavigateBackClick()
            ScanResultAction.OnShareClick -> onShareClick()
            ScanResultAction.OnCopyClick -> onCopyClick()
        }
    }

    private fun onCopyClick() {
        viewModelScope.launch {
            eventChannel.send(
                ScanResultEvent.CopyToClipboard(
                    text = state.value.qr.toFormattedUiText()
                )
            )
        }
    }

    private fun onShareClick() {
        viewModelScope.launch {
            eventChannel.send(
                ScanResultEvent.ShareTo(
                    text = state.value.qr.toFormattedUiText()
                )
            )
        }
    }

    private fun onNavigateBackClick() {
        viewModelScope.launch {
            updateQRTitle(titleState.text.toString())
            eventChannel.send(ScanResultEvent.NavigateBack)
        }
    }
}