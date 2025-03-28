package com.example.antiquecollector.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

/**
 * Room database entity representing a user preference setting.
 */
@Entity(
    tableName = "user_preferences",
    indices = [Index(value = ["key"], unique = true)]
)
data class UserPreferenceEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val key: String,
    val value: String,
    val lastModified: Date = Date()
)