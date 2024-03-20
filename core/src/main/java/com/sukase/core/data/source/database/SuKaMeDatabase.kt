package com.sukase.core.data.source.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sukase.core.data.model.chat.entity.ChatEntity
import com.sukase.core.data.model.conversation.entity.ConversationEntity
import com.sukase.core.data.model.register.entity.AccountEntity

@Database(
    entities = [AccountEntity::class, ConversationEntity::class, ChatEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(SuKaMeConverters::class)
abstract class SuKaMeDatabase : RoomDatabase() {
    abstract fun suKaMeDao(): SuKaMeDao
}