package com.fomaxtro.core.presentation.screen.scan_result

import android.graphics.Bitmap
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fomaxtro.core.domain.model.ImageType
import com.fomaxtro.core.domain.model.QrCodeEntry
import com.fomaxtro.core.domain.qr.QrParser
import com.fomaxtro.core.domain.repository.FileRepository
import com.fomaxtro.core.domain.repository.QrCodeRepository
import com.fomaxtro.core.domain.util.Result
import com.fomaxtro.core.domain.validator.ScanResultValidator
import com.fomaxtro.core.presentation.R
import com.fomaxtro.core.presentation.mapper.toFormattedUiText
import com.fomaxtro.core.presentation.mapper.toUiText
import com.fomaxtro.core.presentation.qr.QrGenerator
import com.fomaxtro.core.presentation.ui.UiText
import com.fomaxtro.core.presentation.util.compressToByteArray
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ScanResultViewModel(
    id: Long,
    private val qrParser: QrParser,
    private val qrCodeRepository: QrCodeRepository,
    private val validator: ScanResultValidator,
    private val fileRepository: FileRepository
) : ViewModel() {
    private var firstLaunch = false
    private lateinit var qrEntry: QrCodeEntry

    private val _state = MutableStateFlow(ScanResultState())
    val state = _state
        .onStart {
            if (!firstLaunch) {
                loadQR(id)
                observeQrCodeEntry()

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

                val qrImage = QrGenerator.generate(
                    qrParser.convertToString(qrEntry.qrCode)
                )

                titleState.setTextAndPlaceCursorAtEnd(qrEntry.title ?: "")
                _state.update {
                    it.copy(
                        qr = qrEntry.qrCode,
                        qrImage = qrImage.asImageBitmap(),
                        isLoading = false,
                        isFavourite = qrEntry.favourite
                    )
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    private fun observeQrCodeEntry() {
        val titleFlow = snapshotFlow { titleState.text.toString() }
            .transformLatest { title ->
                if (validator.isValidTitleLength(title)) {
                    emit(title)
                } else {
                    titleState.setTextAndPlaceCursorAtEnd(title.dropLast(1))
                }
            }
            .debounce(500L)
            .distinctUntilChanged()

        val favouriteFlow = state
            .map { it.isFavourite }
            .distinctUntilChanged()

        combine(titleFlow, favouriteFlow) { title, favourite ->
            qrEntry.copy(
                title = title.ifEmpty { null },
                favourite = favourite
            )
        }
            .drop(1)
            .onEach { entry ->
                qrCodeRepository.save(entry)
            }
            .launchIn(viewModelScope)
    }

    fun onAction(action: ScanResultAction) {
        when (action) {
            ScanResultAction.OnNavigateBackClick -> onNavigateBackClick()
            ScanResultAction.OnShareClick -> onShareClick()
            ScanResultAction.OnCopyClick -> onCopyClick()
            is ScanResultAction.OnFavouriteToggle -> onFavoriteToggle(action.favourite)
            ScanResultAction.OnSaveClick -> onSaveClick()
        }
    }

    private fun onSaveClick() {
        viewModelScope.launch {
            state.value.qrImage?.let { qrImage ->
                val isDownloaded = fileRepository.saveImageToDownloads(
                    imageBytes = qrImage
                        .asAndroidBitmap()
                        .compressToByteArray(Bitmap.CompressFormat.PNG),
                    type = ImageType.PNG
                )

                if (isDownloaded) {
                    eventChannel.send(
                        ScanResultEvent.ShowMessage(
                            UiText.StringResource(R.string.image_downloaded)
                        )
                    )
                } else {
                    eventChannel.send(
                        ScanResultEvent.ShowSystemMessage(
                            UiText.StringResource(R.string.image_download_error)
                        )
                    )
                }
            }
        }
    }

    private fun onFavoriteToggle(favourite: Boolean) {
        _state.update {
            it.copy(
                isFavourite = favourite
            )
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
                ScanResultEvent.ShareTo(
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