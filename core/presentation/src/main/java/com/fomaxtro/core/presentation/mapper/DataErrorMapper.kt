package com.fomaxtro.core.presentation.mapper

import com.fomaxtro.core.domain.error.DataError
import com.fomaxtro.core.presentation.R
import com.fomaxtro.core.presentation.ui.UiText

fun DataError.toUiText(): UiText {
    return when (this) {
        DataError.DISK_FULL -> UiText.StringResource(R.string.disk_full)
        DataError.UNKNOWN -> UiText.StringResource(R.string.error_unknown)
    }
}