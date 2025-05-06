package com.example.antiquecollector.util

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.antiquecollector.workers.NotificationWorker
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationScheduler @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun schedulePeriodicNotifications(repeatInterval: Long = 24, timeUnit: TimeUnit = TimeUnit.HOURS) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
            .build()
            
        val notificationWorkRequest = PeriodicWorkRequestBuilder<NotificationWorker>(
            repeatInterval, timeUnit
        )
            .setConstraints(constraints)
            .build()
            
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            NotificationWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            notificationWorkRequest
        )
    }
    
    fun cancelPeriodicNotifications() {
        WorkManager.getInstance(context).cancelUniqueWork(NotificationWorker.WORK_NAME)
    }
}