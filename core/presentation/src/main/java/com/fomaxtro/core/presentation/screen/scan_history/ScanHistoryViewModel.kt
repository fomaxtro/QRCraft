package com.fomaxtro.core.presentation.screen.scan_history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fomaxtro.core.domain.model.QrCodeEntry
import com.fomaxtro.core.domain.model.QrCodeSource
import com.fomaxtro.core.domain.repository.QrCodeRepository
import com.fomaxtro.core.domain.util.Result
import com.fomaxtro.core.presentation.mapper.toFormattedUiText
import com.fomaxtro.core.presentation.mapper.toQrCodeUi
import com.fomaxtro.core.presentation.mapper.toUiText
import com.fomaxtro.core.presentation.model.QrCodeUi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class ScanHistoryViewModel(
    private val qrCodeRepository: QrCodeRepository
) : ViewModel() {
    private var firstLaunch = false
    private var selectedQrHistoryItem: QrCodeUi? = null
    private var qrCodeEntries = emptyList<QrCodeEntry>()

    private val _state = MutableStateFlow(ScanHistoryState())
    val state = _state
        .onStart {
            if (!firstLaunch) {
                observeTabIndex()

                firstLaunch = true
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            ScanHistoryState()
        )

    private val eventChannel = Channel<ScanHistoryEvent>()
    val events = eventChannel.receiveAsFlow()

    @Suppress("UnusedFlow")
    private fun observeTabIndex() {
        state
            .map { it.selectedTabIndex }
            .distinctUntilChanged()
            .flatMapLatest { tabIndex ->
                val source = when (tabIndex) {
                    0 -> QrCodeSource.SCANNED
                    1 -> QrCodeSource.GENERATED
                    else -> return@flatMapLatest emptyFlow()
                }

                qrCodeRepository.findAllRecentBySource(source)
            }
            .onEach { entries ->
                qrCodeEntries = entries

                _state.update { state ->
                    state.copy(
                        history = entries.map { it.toQrCodeUi() }
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun onAction(action: ScanHistoryAction) {
        when (action) {
            is ScanHistoryAction.OnTabSelected -> onTabSelected(action.tabIndex)
            ScanHistoryAction.OnBottomSheetDismiss -> onBottomSheetDismiss()
            is ScanHistoryAction.OnHistoryLongClick -> onHistoryLongClick(action.qrCode)
            ScanHistoryAction.OnDeleteClick -> onDeleteClick()
            ScanHistoryAction.OnShareClick -> onShareClick()
            is ScanHistoryAction.OnHistoryClick -> onHistoryClick(action.qrCode)
            is ScanHistoryAction.OnFavouriteChange -> onFavouriteChange(action.id, action.favourite)
        }
    }

    private fun onFavouriteChange(id: Long, favourite: Boolean) {
        val entry = qrCodeEntries.find { it.id == id } ?: return

        viewModelScope.launch {
            qrCodeRepository.save(entry.copy(favourite = favourite))
        }
    }

    private fun onHistoryClick(qrCode: QrCodeUi) {
        viewModelScope.launch {
            eventChannel.send(ScanHistoryEvent.NavigateToScanResult(qrCode.id))
        }
    }

    private fun onShareClick() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isShareSheetVisible = false
                )
            }

            val formattedQr = selectedQrHistoryItem?.qrCode?.toFormattedUiText() ?: return@launch

            eventChannel.send(ScanHistoryEvent.ShareTo(formattedQr))

            selectedQrHistoryItem = null
        }
    }

    private fun onDeleteClick() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isShareSheetVisible = false
                )
            }

            selectedQrHistoryItem?.let {
                when (val result = qrCodeRepository.deleteById(it.id)) {
                    is Result.Error -> {
                        eventChannel.send(
                            ScanHistoryEvent.ShowSystemError(
                                result.error.toUiText()
                            )
                        )
                    }

                    else -> Unit
                }
            }

            selectedQrHistoryItem = null
        }
    }

    private fun onHistoryLongClick(qrCode: QrCodeUi) {
        selectedQrHistoryItem = qrCode

        _state.update {
            it.copy(
                isShareSheetVisible = true
            )
        }
    }

    private fun onBottomSheetDismiss() {
        _state.update {
            it.copy(
                isShareSheetVisible = false
            )
        }
    }

    private fun onTabSelected(tabIndex: Int) {
        _state.update {
            it.copy(
                selectedTabIndex = tabIndex
            )
        }
    }
}