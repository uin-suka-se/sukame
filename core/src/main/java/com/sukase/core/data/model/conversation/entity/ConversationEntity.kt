package com.sukase.core.data.model.conversation.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "conversation")
data class ConversationEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo
    val id: String,
    val name: String,
    val photo: String,
    val type: String,
    val lastMessage: String?,
    val dateTime: Long?,
    val participantsId: Set<Int>,
    val participantsUsername: Set<String>,
    val participantsFullName: List<String>,
)
