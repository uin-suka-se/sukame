package com.sukase.sukame.ui.base

import android.content.Context
import android.widget.Toast
import com.sukase.core.utils.UiText
import com.sukase.core.utils.UiText.Companion.asString


fun showToast(context: Context, message: UiText) {
    Toast.makeText(
        context,
        message.asString(context),
        Toast.LENGTH_LONG
    ).show()
}