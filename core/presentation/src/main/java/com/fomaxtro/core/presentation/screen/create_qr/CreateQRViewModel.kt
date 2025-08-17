package com.fomaxtro.core.presentation.screen.create_qr

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class CreateQRViewModel : ViewModel() {
    private val eventChannel = Channel<CreateQREvent>()
    val events = eventChannel.receiveAsFlow()

    fun onAction(action: CreateQRAction) {
        when (action) {
            CreateQRAction.OnContactClick -> {
                viewModelScope.launch {
                    eventChannel.send(CreateQREvent.NavigateToCreateContactQR)
                }
            }

            CreateQRAction.OnGeolocationClick -> {
                viewModelScope.launch {
                    eventChannel.send(CreateQREvent.NavigateToCreateGeolocationQR)
                }
            }

            CreateQRAction.OnLinkClick -> {
                viewModelScope.launch {
                    eventChannel.send(CreateQREvent.NavigateToCreateLinkQR)
                }
            }

            CreateQRAction.OnPhoneClick -> {
                viewModelScope.launch {
                    eventChannel.send(CreateQREvent.NavigateToCreatePhoneQR)
                }
            }

            CreateQRAction.OnTextClick -> {
                viewModelScope.launch {
                    eventChannel.send(CreateQREvent.NavigateToCreateTextQR)
                }
            }

            CreateQRAction.OnWifiClick -> {
                viewModelScope.launch {
                    eventChannel.send(CreateQREvent.NavigateToCreateWifiQR)
                }
            }
        }
    }
}