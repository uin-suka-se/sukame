package com.sukase.sukame.ui.utils


import android.util.Log
import com.sukase.sukame.ui.base.CannotBlankEditText

object EditTextUtils {
    fun isNotBlankEdtField(vararg edt: CannotBlankEditText): Boolean {
        for ((index, value) in edt.withIndex()) {
            return if (index == edt.lastIndex) {
                value.error.isNullOrBlank()
            } else {
                if (value.error.isNullOrBlank()) {
                    Log.d("register", "continue")
                    continue
                } else break
            }
        }
        Log.d("register", "masuk false")
        return false
    }
}