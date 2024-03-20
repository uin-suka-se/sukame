package com.sukase.core.data.model.register.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "account",
    indices = [Index(
        value = ["username"],
        unique = true
    )]
)
data class AccountEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val username: String,
    val fullName: String,
)
