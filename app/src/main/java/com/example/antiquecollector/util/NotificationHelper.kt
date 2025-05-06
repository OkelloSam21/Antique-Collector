package com.example.antiquecollector.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.antiquecollector.R
import com.example.antiquecollector.workers.NotificationWorker
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationHelper @Inject constructor(
    private val context: Context
) {
    companion object {
        const val CHANNEL_ID = "antique_collector_channel"
        const val CHANNEL_NAME = "Antique Collector"
        const val CHANNEL_DESCRIPTION = "Notifications from Antique Collector"

        // Static methods for direct access from settings and other components
        fun scheduleDailyNotification(context: Context) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
                .build()

            val notificationWorkRequest = PeriodicWorkRequestBuilder<NotificationWorker>(
                24, TimeUnit.HOURS
            )
                .setConstraints(constraints)
                .build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                NotificationWorker.WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                notificationWorkRequest
            )
        }

        fun cancelScheduledNotification(context: Context) {
            WorkManager.getInstance(context).cancelUniqueWork(NotificationWorker.WORK_NAME)
        }
    }

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = CHANNEL_DESCRIPTION
            }

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showNotification(title: String, content: String, notificationId: Int) {
        val intent = android.content.Intent(context, com.example.antiquecollector.MainActivity::class.java).apply {
            flags = android.content.Intent.FLAG_ACTIVITY_NEW_TASK or android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = android.app.PendingIntent.getActivity(
            context, 0, intent,
            android.app.PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_art)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            try {
                notify(notificationId, builder.build())
            } catch (e: SecurityException) {
                // Handle missing notification permission
            }
        }
    }

    // Instance methods that delegate to static methods
    fun schedulePeriodicNotifications() {
        scheduleDailyNotification(context)
    }

    fun cancelPeriodicNotifications() {
        cancelScheduledNotification(context)
    }
}