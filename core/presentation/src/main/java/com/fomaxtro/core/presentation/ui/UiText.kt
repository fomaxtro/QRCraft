package com.fomaxtro.core.presentation.ui

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource

@Stable
sealed interface UiText {
    data class StringResource(
        @field:StringRes val resId: Int,
        val args: Array<Any> = emptyArray()
    ) : UiText {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as StringResource

            if (resId != other.resId) return false
            if (!args.contentEquals(other.args)) return false

            return true
        }

        override fun hashCode(): Int {
            var result = resId
            result = 31 * result + args.contentHashCode()
            return result
        }
    }

    data class DynamicString(val value: String) : UiText

    data class Chained(
        val texts: List<UiText>,
        val separator: String = "\n"
    ) : UiText

    fun asString(context: Context): String {
        return when (this) {
            is StringResource -> context.getString(resId, *args)
            is DynamicString -> value
            is Chained -> texts.joinToString(separator) { it.asString(context) }
        }
    }

    @Composable
    fun asString(): String {
        return when (this) {
            is StringResource -> stringResource(resId, *args)
            is DynamicString -> value
            is Chained -> {
                val context = LocalContext.current

                texts.joinToString(separator) { it.asString(context) }
            }
        }
    }
}