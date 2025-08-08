package com.fomaxtro.core.presentation.screen.scan_result

import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fomaxtro.core.domain.FileManager
import com.fomaxtro.core.presentation.mapper.toFormattedText
import com.fomaxtro.core.presentation.model.QR
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

class ScanResultViewModel(
    qr: QR,
    imagePath: String,
    private val fileManager: FileManager
) : ViewModel() {
    private val _state = MutableStateFlow(
        ScanResultState(
            qr = qr
        )
    )
    val state = _state
        .onStart {
            loadImage(imagePath)
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000L),
            ScanResultState(
                qr = qr
            )
        )

    init {
        Timber.tag("NavigationRoot").d(
            """
                            qr: $qr
                            imagePath: $imagePath
                        """.trimIndent()
        )
    }

    private val eventChannel = Channel<ScanResultEvent>()
    val events = eventChannel.receiveAsFlow()

    private suspend fun loadImage(imagePath: String) {
        val imageBytes = fileManager.readImageAndDelete(imagePath)
        val qrImage = BitmapFactory
            .decodeByteArray(imageBytes, 0, imageBytes.size)
            .asImageBitmap()

        _state.update {
            it.copy(
                qrImage = qrImage
            )
        }
    }

    fun onAction(action: ScanResultAction) {
        when (action) {
            ScanResultAction.OnNavigateBackClick -> onNavigateBackClick()
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

    private fun onNavigateBackClick() {
        viewModelScope.launch {
            eventChannel.send(ScanResultEvent.NavigateBack)
        }
    }
}