package com.example.antiquecollector.data.local

import androidx.room.TypeConverter
import com.example.antiquecollector.domain.model.NotificationType
import java.util.Date

/**
 * Type converters for Room database to handle complex types.
 */
class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromStringList(value: List<String>?): String? {
        return value?.joinToString(separator = ",")
    }

    @TypeConverter
    fun toStringList(value: String?): List<String>? {
        return value?.split(",")?.map { it.trim() }?.filter { it.isNotEmpty() }
    }
    
    @TypeConverter
    fun fromNotificationType(type: NotificationType): String {
        return type.name
    }
    
    @TypeConverter
    fun toNotificationType(value: String): NotificationType {
        return try {
            NotificationType.valueOf(value)
        } catch (e: Exception) {
            NotificationType.SYSTEM
        }
    }
    @TypeConverter
    fun fromStringToLong(value: String?): Long? {
        return value?.toLongOrNull() //Safely convert to Long, handling nulls
    }

    @TypeConverter
    fun fromLongToString(value: Long?): String? {
        return value?.toString()
    }

}