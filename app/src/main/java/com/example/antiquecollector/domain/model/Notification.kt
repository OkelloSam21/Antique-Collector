package com.example.antiquecollector.domain.model

import java.util.Date

/**
 * Domain model representing a notification.
 */
data class Notification(
    val id: Long = 0,
    val title: String,
    val content: String,
    val type: NotificationType,
    val createdAt: Date = Date(),
    val isRead: Boolean = false
)