package com.sukase.core.utils

import android.content.Context
import androidx.annotation.StringRes

sealed class UiText {
    data class DynamicString(val value: String) : UiText()
    class StringResource(@StringRes val id: Int, vararg val args: Any) : UiText()

    companion object {
        fun UiText.asString(context: Context) =
            when (this) {
                is DynamicString -> value
                is StringResource -> context.getString(id, *args)
            }
    }
}