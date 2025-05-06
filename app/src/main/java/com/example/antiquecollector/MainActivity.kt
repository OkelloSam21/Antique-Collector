package com.example.antiquecollector

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.antiquecollector.data.local.AppDatabase
import com.example.antiquecollector.domain.repository.PreferenceRepository
import com.example.antiquecollector.ui.screens.onboarding.OnboardingPreferences
import com.example.antiquecollector.ui.theme.AntiqueCollectorTheme
import com.example.antiquecollector.util.SplashScreenManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Main activity for the Antique Collector app.
 * Uses Jetpack Compose for the UI and Hilt for dependency injection.
 */

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var onboardingPreferences: OnboardingPreferences

    @Inject
    lateinit var dataBase: AppDatabase

    @Inject
    lateinit var preferenceRepository: PreferenceRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize SplashScreenManager
        SplashScreenManager(this, onboardingPreferences, lifecycleScope)

        lifecycleScope.launch {
            delay(1500) // Short delay to allow UI to settle
            requestNotificationPermission()
        }

        setContent {
            // Create a flow to observe dark mode preference
            val darkModeFlow = flow {
                while(true) {
                    val isDarkMode = preferenceRepository.getPreferenceValue("dark_mode", "false") == "true"
                    emit(isDarkMode)
                    delay(300) // Check every 300ms
                }
            }

            // Collect the dark mode preference
            val isDarkMode = darkModeFlow.collectAsState(initial = isSystemInDarkTheme())

            AntiqueCollectorTheme(isDarkThemeEnabled = isDarkMode.value) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppRoute(onboardingPreferences = onboardingPreferences)
                }
            }
        }

        val dbPath = applicationContext.getDatabasePath("antique_collector_database").absolutePath
        Log.d("MainActivity", "Database path : $dbPath")
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    NOTIFICATION_PERMISSION_REQUEST_CODE
                )
            }
        }
    }

    companion object {
        private const val NOTIFICATION_PERMISSION_REQUEST_CODE = 123
    }
}