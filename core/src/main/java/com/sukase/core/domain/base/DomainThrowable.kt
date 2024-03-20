package com.sukase.core.domain.base

import com.sukase.core.utils.UiText

data class DomainThrowable(
    val type: String,
    val throwMessage: UiText,
): Throwable() {
    fun mapToUiText(): UiText {
        return throwMessage
    }
}