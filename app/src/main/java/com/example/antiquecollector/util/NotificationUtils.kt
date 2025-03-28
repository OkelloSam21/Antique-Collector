package com.example.antiquecollector.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.antiquecollector.MainActivity
import com.example.antiquecollector.R
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Utility class for handling notifications.
 */
@Singleton
class NotificationUtils @Inject constructor(
    private val context: Context
) {
    
    companion object {
        // Notification channel IDs
        const val CHANNEL_COLLECTION_REVIEW = "collection_review"
        const val CHANNEL_MUSEUM_EVENTS = "museum_events"
        
        // Notification IDs
        const val NOTIFICATION_ID_COLLECTION_REVIEW = 1
        const val NOTIFICATION_ID_MUSEUM_EVENT = 2
    }
    
    init {
        createNotificationChannels()
    }
    
    /**
     * Creates the notification channels for the app.
     */
    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Collection review channel
            val collectionReviewChannel = NotificationChannel(
                CHANNEL_COLLECTION_REVIEW,
                context.getString(R.string.notification_channel_collection_review),
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = context.getString(R.string.notification_channel_collection_review_description)
            }
            
            // Museum events channel
            val museumEventsChannel = NotificationChannel(
                CHANNEL_MUSEUM_EVENTS,
                context.getString(R.string.notification_channel_museum_events),
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = context.getString(R.string.notification_channel_museum_events_description)
            }
            
            // Register the channels with the system
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannels(listOf(collectionReviewChannel, museumEventsChannel))
        }
    }
    
    /**
     * Shows a collection review notification.
     *
     * @param title The notification title
     * @param content The notification content
     */
    @androidx.annotation.RequiresPermission(
        android.Manifest.permission.POST_NOTIFICATIONS
    )
    fun showCollectionReviewNotification(title: String, content: String) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
        
        val notification = NotificationCompat.Builder(context, CHANNEL_COLLECTION_REVIEW)
//            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
        
        with(NotificationManagerCompat.from(context)) {
            notify(NOTIFICATION_ID_COLLECTION_REVIEW, notification)
        }
    }
    
    /**
     * Shows a museum event notification.
     *
     * @param title The notification title
     * @param content The notification content
     */
    @androidx.annotation.RequiresPermission(
        android.Manifest.permission.POST_NOTIFICATIONS
    )
    fun showMuseumEventNotification(title: String, content: String) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
        
        val notification = NotificationCompat.Builder(context, CHANNEL_MUSEUM_EVENTS)
//            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
        
        with(NotificationManagerCompat.from(context)) {
            notify(NOTIFICATION_ID_MUSEUM_EVENT, notification)
        }
    }
    
    /**
     * Cancels a notification.
     *
     * @param notificationId The ID of the notification to cancel
     */
    fun cancelNotification(notificationId: Int) {
        with(NotificationManagerCompat.from(context)) {
            cancel(notificationId)
        }
    }
    
    /**
     * Cancels all notifications.
     */
    fun cancelAllNotifications() {
        with(NotificationManagerCompat.from(context)) {
            cancelAll()
        }
    }
}