package com.sukase.core.data.base

import com.sukase.core.domain.base.DomainThrowable
import com.sukase.core.utils.UiText

data class DatabaseException(
    val exceptionMessage: UiText
) : Exception() {
    fun mapToDomainThrowable(): DomainThrowable {
        return DomainThrowable("Exception", exceptionMessage)
    }
}
