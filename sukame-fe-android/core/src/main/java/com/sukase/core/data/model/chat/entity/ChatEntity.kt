package com.sukase.core.data.model.chat.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "chat"
)
data class ChatEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo
    val id: Int = 0,
    val conversationId: String,
    val photo: String,
    val type: String,
    val message: String,
    val datetime: Long,
    val senderId: Int,
    val senderUsername: String,
    val senderFullName: String,
    val receiverId: Set<Int>,
    val receiverUsername: Set<String>,
    val receiverFullName: List<String>
)