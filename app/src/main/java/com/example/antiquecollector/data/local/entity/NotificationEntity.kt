package com.example.antiquecollector.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.antiquecollector.domain.model.NotificationType
import java.util.Date

/**
 * Room database entity representing a notification.
 */
@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val content: String,
    val type: NotificationType,
    val createdAt: Date = Date(),
    val isRead: Boolean = false
)