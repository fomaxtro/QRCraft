package com.fomaxtro.core.presentation.screen.scan_result

import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fomaxtro.core.domain.qr.QRParser
import com.fomaxtro.core.domain.repository.QRCodeRepository
import com.fomaxtro.core.domain.util.Result
import com.fomaxtro.core.presentation.mapper.toFormattedUiText
import com.fomaxtro.core.presentation.mapper.toUiText
import com.fomaxtro.core.presentation.util.QRGenerator
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ScanResultViewModel(
    id: Long,
    private val qrParser: QRParser,
    private val qrCodeRepository: QRCodeRepository
) : ViewModel() {
    private var firstLaunch = false

    private val _state = MutableStateFlow(ScanResultState())
    val state = _state
        .onStart {
            if (!firstLaunch) {
                loadQR(id)

                firstLaunch = true
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000L),
            ScanResultState()
        )

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
                val qrImage = QRGenerator.generate(
                    qrParser.convertToString(entry.data.qrCode)
                )

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
                ScanResultEvent.ShareText(
                    text = state.value.qr.toFormattedUiText()
                )
            )
        }
    }

    private fun onNavigateBackClick() {
        viewModelScope.launch {
            eventChannel.send(ScanResultEvent.NavigateBack)
        }
    }
}