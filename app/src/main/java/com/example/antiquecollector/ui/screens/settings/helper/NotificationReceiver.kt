package com.example.antiquecollector.ui.screens.settings.helper

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // Show your notification here
        NotificationHelper.showNotification(
            context,
            "Daily Reminder",
            "Don't forget to check your Antique Collection!"
        )
    }
}