package com.sukase.sukame.ui.base

import android.content.Context
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.sukase.core.utils.UiText
import com.sukase.core.utils.UiText.Companion.asString

fun showSnackBar(view: View, context: Context, message: UiText) {
    Snackbar.make(
        view,
        message.asString(context),
        Snackbar.LENGTH_SHORT
    ).show()
}