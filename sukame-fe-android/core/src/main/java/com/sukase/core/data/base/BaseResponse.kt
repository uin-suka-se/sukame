package com.sukase.core.data.base

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BaseResponse<T>(
    @SerialName("results")
    val data: T,
    @SerialName("status_code")
    val statusCode: String,
    @SerialName("status_message")
    val statusMessage: String
)
