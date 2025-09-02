package com.fomaxtro.core.presentation.screen.scan_result

import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fomaxtro.core.domain.qr.QRParser
import com.fomaxtro.core.presentation.mapper.toFormattedText
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
    qr: String,
    private val qrParser: QRParser
) : ViewModel() {
    private var firstLaunch = false

    private val _state = MutableStateFlow(initState(qr))
    val state = _state
        .onStart {
            if (!firstLaunch) {
                loadImage(qr)

                firstLaunch = true
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000L),
            initState(qr)
        )

    private val eventChannel = Channel<ScanResultEvent>()
    val events = eventChannel.receiveAsFlow()

    private fun initState(qr: String): ScanResultState {
        return ScanResultState(
            qr = qrParser.parseFromString(qr)
        )
    }

    private suspend fun loadImage(qr: String) {
        val qrImage = QRGenerator.generate(qr)

        _state.update {
            it.copy(
                qrImage = qrImage.asImageBitmap()
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
                    text = state.value.qr.toFormattedText()
                )
            )
        }
    }

    private fun onShareClick() {
        viewModelScope.launch {
            eventChannel.send(
                ScanResultEvent.ShareText(
                    text = state.value.qr.toFormattedText()
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