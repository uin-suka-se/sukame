package com.sukase.core.domain.model

data class UserModel(
    val id: String,
    val username: String,
    val fullName: String,
    val token: String,
)
