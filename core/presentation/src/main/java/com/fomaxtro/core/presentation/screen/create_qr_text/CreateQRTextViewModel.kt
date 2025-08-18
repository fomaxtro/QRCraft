package com.fomaxtro.core.presentation.screen.create_qr_text

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow

class CreateQRTextViewModel : ViewModel() {
    private val _state = MutableStateFlow(CreateQRTextState())
    val state = _state.asStateFlow()

    private val eventChannel = Channel<CreateQRTextEvent>()
    val events = eventChannel.receiveAsFlow()

    fun onAction(action: CreateQRTextAction) {
    }
}