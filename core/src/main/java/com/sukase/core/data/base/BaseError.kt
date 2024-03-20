package com.sukase.core.data.base

import com.sukase.core.domain.base.DomainThrowable
import com.sukase.core.utils.UiText

data class BaseError(
    val errorMessage: UiText
) : Error() {
    fun mapToDomainThrowable(): DomainThrowable {
        return DomainThrowable("Error", errorMessage)
    }
}
