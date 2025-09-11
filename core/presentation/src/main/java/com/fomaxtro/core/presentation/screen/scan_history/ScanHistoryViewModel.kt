package com.fomaxtro.core.presentation.screen.scan_history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fomaxtro.core.domain.model.QRCodeSource
import com.fomaxtro.core.domain.repository.QRCodeRepository
import com.fomaxtro.core.domain.util.Result
import com.fomaxtro.core.presentation.mapper.toFormattedUiText
import com.fomaxtro.core.presentation.mapper.toQRCodeUi
import com.fomaxtro.core.presentation.mapper.toUiText
import com.fomaxtro.core.presentation.model.QRCodeUi
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

class ScanHistoryViewModel(
    private val qrCodeRepository: QRCodeRepository
) : ViewModel() {
    private var firstLaunch = false
    private var selectedQrHistoryItem: QRCodeUi? = null

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

    private fun observeTabIndex() {
        state
            .map { it.selectedTabIndex }
            .distinctUntilChanged()
            .onEach { loadHistoryForTab(it) }
            .launchIn(viewModelScope)
    }

    private suspend fun loadHistoryForTab(tabIndex: Int) {
        val source = when (tabIndex) {
            0 -> QRCodeSource.SCANNED
            1 -> QRCodeSource.GENERATED
            else -> throw IllegalArgumentException("Invalid tab index: $tabIndex")
        }

        when (val result = qrCodeRepository.findAllRecentBySource(source)) {
            is Result.Error -> {
                eventChannel.send(
                    ScanHistoryEvent.ShowSystemError(
                        result.error.toUiText()
                    )
                )
            }

            is Result.Success -> {
                _state.update { state ->
                    state.copy(
                        history = result.data.map { it.toQRCodeUi() }
                    )
                }
            }
        }
    }

    fun onAction(action: ScanHistoryAction) {
        when (action) {
            is ScanHistoryAction.OnTabSelected -> onTabSelected(action.tabIndex)
            ScanHistoryAction.OnBottomSheetDismiss -> onBottomSheetDismiss()
            is ScanHistoryAction.OnHistoryLongClick -> onHistoryLongClick(action.qrCode)
            ScanHistoryAction.OnDeleteClick -> onDeleteClick()
            ScanHistoryAction.OnShareClick -> onShareClick()
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

    }

    private fun onHistoryLongClick(qrCode: QRCodeUi) {
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