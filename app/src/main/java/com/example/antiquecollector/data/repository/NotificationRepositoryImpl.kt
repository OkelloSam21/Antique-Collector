package com.example.antiquecollector.data.repository

import com.example.antiquecollector.data.local.dao.NotificationDao
import com.example.antiquecollector.data.local.entity.NotificationEntity
import com.example.antiquecollector.domain.model.Notification
import com.example.antiquecollector.domain.repository.NotificationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Implementation of the NotificationRepository interface.
 */
class NotificationRepositoryImpl @Inject constructor(
    private val notificationDao: NotificationDao
) : NotificationRepository {
    
    override suspend fun getNotificationById(id: Long): Notification? {
        val entity = notificationDao.getNotificationById(id) ?: return null
        return Notification(
            id = entity.id,
            title = entity.title,
            content = entity.content,
            type = entity.type,
            createdAt = entity.createdAt,
            isRead = entity.isRead
        )
    }
    
    override fun getAllNotifications(): Flow<List<Notification>> {
        return notificationDao.getAllNotifications().map { entities ->
            entities.map { entity ->
                Notification(
                    id = entity.id,
                    title = entity.title,
                    content = entity.content,
                    type = entity.type,
                    createdAt = entity.createdAt,
                    isRead = entity.isRead
                )
            }
        }
    }
    
    override fun getUnreadNotifications(): Flow<List<Notification>> {
        return notificationDao.getUnreadNotifications().map { entities ->
            entities.map { entity ->
                Notification(
                    id = entity.id,
                    title = entity.title,
                    content = entity.content,
                    type = entity.type,
                    createdAt = entity.createdAt,
                    isRead = entity.isRead
                )
            }
        }
    }
    
    override suspend fun addNotification(notification: Notification): Long {
        val entity = NotificationEntity(
            title = notification.title,
            content = notification.content,
            type = notification.type,
            createdAt = notification.createdAt,
            isRead = notification.isRead
        )
        return notificationDao.insertNotification(entity)
    }
    
    override suspend fun markNotificationAsRead(id: Long) {
        notificationDao.markAsRead(id)
    }
    
    override suspend fun markAllNotificationsAsRead() {
        notificationDao.markAllAsRead()
    }
    
    override suspend fun deleteNotification(id: Long) {
        notificationDao.deleteNotificationById(id)
    }
    
    override suspend fun deleteAllNotifications() {
        notificationDao.deleteAllNotifications()
    }
}