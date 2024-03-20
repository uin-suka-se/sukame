package com.sukase.core.data.mapper

import com.sukase.core.UserPreferences
import com.sukase.core.domain.model.UserModel

fun UserPreferences.mapToUserModel() = UserModel(id = uid, username = username, fullName = token, token = token)