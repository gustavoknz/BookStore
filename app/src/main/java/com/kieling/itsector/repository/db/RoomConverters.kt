package com.kieling.itsector.repository.db

import androidx.room.TypeConverter

/**
 * Type converters to allow Room to reference complex data types
 */
class RoomConverters {
    @TypeConverter
    fun fromString(string: String): List<String> = string.split(",").map { it }

    @TypeConverter
    fun toString(list: List<String>): String = list.joinToString(separator = ",")
}
