package com.example.antiquecollector.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.antiquecollector.domain.model.Notification
import com.example.antiquecollector.domain.model.NotificationType
import com.example.antiquecollector.domain.repository.AntiqueRepository
import com.example.antiquecollector.domain.repository.NotificationRepository
import com.example.antiquecollector.util.NotificationHelper
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.util.Date

@HiltWorker
class NotificationWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val notificationHelper: NotificationHelper,
    private val notificationRepository: NotificationRepository,
    private val antiqueRepository: AntiqueRepository
) : CoroutineWorker(context, params) {

    companion object {
        const val WORK_NAME = "antique_collector_notifications"
        const val NOTIFICATION_ID = 1000
    }

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            // Get collection statistics
            val statistics = antiqueRepository.getCollectionStatistics().first()
            
            // Generate notification content based on app data
            val title = "Your Antique Collection Update"
            val content = when {
                statistics.totalItems == 0 -> 
                    "Start adding items to your collection today!"
                
                statistics.recentAdditions.isNotEmpty() ->
                    "You've added ${statistics.recentAdditions} new items recently. Your collection now has ${statistics.totalItems} items worth ${statistics.totalValue}."
                
                else -> 
                    "Your collection has ${statistics.totalItems} items with an estimated value of ${statistics.totalValue}."
            }
            
            // Store notification in database
            val notification = Notification(
                id = 0,  // Auto-generated
                title = title,
                content = content,
                createdAt = Date(),  // Changed from Date().time to Date object
                isRead = false,
                type = NotificationType.COLLECTION_REVIEW // Changed from string to enum value
            )
            val notificationId = notificationRepository.addNotification(notification)
            
            // Show actual system notification
            notificationHelper.showNotification(title, content, NOTIFICATION_ID)
            
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}