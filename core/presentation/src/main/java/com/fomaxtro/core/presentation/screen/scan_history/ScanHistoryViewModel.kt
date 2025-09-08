package com.fomaxtro.core.presentation.screen.scan_history

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow

class ScanHistoryViewModel : ViewModel() {
    private val _state = MutableStateFlow(ScanHistoryState())
    val state = _state.asStateFlow()

    private val eventChannel = Channel<ScanHistoryEvent>()
    val events = eventChannel.receiveAsFlow()

    fun onAction(action: ScanHistoryAction) {
    }
}