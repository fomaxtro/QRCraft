package com.fomaxtro.core.presentation.ui

import android.content.Context
import androidx.annotation.StringRes

sealed interface UiText {
    data class StringResource(@field:StringRes val resId: Int) : UiText

    fun asString(context: Context): String {
        return when (this) {
            is StringResource -> context.getString(resId)
        }
    }
}