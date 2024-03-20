package com.sukase.core.data.source.database

import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class SuKaMeConverters {
    @TypeConverter
    fun fromListOfInt(value : List<Int>) = Json.encodeToString(value)

    @TypeConverter
    fun toListOfInt(value: String) = Json.decodeFromString<List<Int>>(value)

    @TypeConverter
    fun fromListOfString(value : List<String>) = Json.encodeToString(value)

    @TypeConverter
    fun toListOfString(value: String) = Json.decodeFromString<List<String>>(value)

    @TypeConverter
    fun fromSetOfInt(value : Set<Int>) = Json.encodeToString(value)

    @TypeConverter
    fun toSetOfInt(value: String) = Json.decodeFromString<Set<Int>>(value)

    @TypeConverter
    fun fromSetOfString(value : Set<String>) = Json.encodeToString(value)

    @TypeConverter
    fun toSetOfString(value: String) = Json.decodeFromString<Set<String>>(value)
}