package com.example.antiquecollector


import android.app.Application
import com.example.antiquecollector.util.NotificationHelper
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

/**
 * Application class for the Antique Collector app.
 * The HiltAndroidApp annotation triggers Hilt's code generation.
 */
@HiltAndroidApp
class AntiqueCollectorApp : Application() {

    @Inject
    lateinit var notificationHelper: NotificationHelper

    override fun onCreate() {
        super.onCreate()

        // Only schedule notifications if they're enabled in settings
        val sharedPrefs = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val notificationsEnabled = sharedPrefs.getBoolean("notification_enabled", true)

        if (notificationsEnabled) {
            notificationHelper.schedulePeriodicNotifications()
        }
    }
}