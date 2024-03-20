package com.sukase.core.data.base

import com.sukase.core.R
import com.sukase.core.domain.base.DomainThrowable
import com.sukase.core.utils.UiText
import kotlinx.serialization.SerialName

data class ApiException(
    @SerialName("status_code")
    val statusCode: String,
    @SerialName("status_message")
    val statusMessage: String
) : Exception() {
    fun mapToDomainThrowable(): DomainThrowable {
        return DomainThrowable(
            "Exception",
            UiText.StringResource(R.string.api_error, statusCode, statusMessage)
        )
    }
}