package com.fomaxtro.core.presentation.screen.create_qr

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow

class CreateQRViewModel : ViewModel() {
    private val _state = MutableStateFlow(CreateQRState())
    val state = _state.asStateFlow()

    private val eventChannel = Channel<CreateQREvent>()
    val events = eventChannel.receiveAsFlow()

    fun onAction(action: CreateQRAction) {
    }
}