package com.fomaxtro.core.presentation.screen.scan_result

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fomaxtro.core.domain.ShareManager
import com.fomaxtro.core.domain.model.QRCodeEntry
import com.fomaxtro.core.domain.qr.QRParser
import com.fomaxtro.core.domain.repository.QRCodeRepository
import com.fomaxtro.core.domain.util.Result
import com.fomaxtro.core.presentation.mapper.toUiText
import com.fomaxtro.core.presentation.util.QRGenerator
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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

class ScanResultViewModel(
    id: Long,
    private val qrParser: QRParser,
    private val qrCodeRepository: QRCodeRepository,
    private val shareManager: ShareManager
) : ViewModel() {
    private var firstLaunch = false
    private var qrEntry: QRCodeEntry? = null

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

    @OptIn(FlowPreview::class)
    private fun observeTitle() {
        snapshotFlow { titleState.text.toString() }
            .debounce(500L)
            .distinctUntilChanged()
            .onEach { title ->
                qrEntry?.let { entry ->
                    Timber.d("Updating entry: $entry")

                    qrCodeRepository.save(
                        entry.copy(
                            title = title.ifEmpty { null }
                        )
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun onAction(action: ScanResultAction) {
        when (action) {
            ScanResultAction.OnNavigateBackClick -> onNavigateBackClick()
            is ScanResultAction.OnShareClick -> onShareClick(action.text)
            is ScanResultAction.OnCopyClick -> onCopyClick(action.text)
        }
    }

    private fun onCopyClick(text: String) {
        shareManager.copyToClipboard(text)
    }

    private fun onShareClick(text: String) {
        shareManager.shareTo(text)
    }

    private fun onNavigateBackClick() {
        viewModelScope.launch {
            eventChannel.send(ScanResultEvent.NavigateBack)
        }
    }
}