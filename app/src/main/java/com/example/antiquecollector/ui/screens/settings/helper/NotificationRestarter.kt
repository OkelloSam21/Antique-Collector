package com.example.antiquecollector.ui.screens.settings.helper// NotificationRestarter.kt

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.content.edit

class NotificationRestarter : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            // Reschedule only if notifications were enabled before reboot.
            val sharedPrefs = context.getSharedPreferences(
                "app_prefs",
                Context.MODE_PRIVATE
            ) // Replace "app_prefs" if you use a different name
            val notificationsEnabled = sharedPrefs.getBoolean("notification_enabled", false)

            if (notificationsEnabled) {
                NotificationHelper.scheduleDailyNotification(context)
            }
        }
    }
}