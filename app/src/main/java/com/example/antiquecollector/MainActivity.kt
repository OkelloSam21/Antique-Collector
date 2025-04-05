package com.example.antiquecollector

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.example.antiquecollector.data.local.AppDatabase
import com.example.antiquecollector.ui.navigation.AppRoute
import com.example.antiquecollector.ui.screens.onboarding.OnboardingPreferences
import com.example.antiquecollector.ui.theme.AntiqueCollectorTheme
import com.example.antiquecollector.util.SplashScreenManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize SplashScreenManager
        SplashScreenManager(this, onboardingPreferences, lifecycleScope)

        lifecycleScope.launch (Dispatchers.IO){
            try {
                val count = dataBase.categoryDao().getCategoryCount().first()
                Log.d("MainActivity", "Database initialized count: $count")
            } catch (e:Exception) {
                Log.d("MainActivity", "Error initializing database")
            }
        }

        setContent {
            AntiqueCollectorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppRoute(onboardingPreferences = onboardingPreferences)
                }
            }
        }

        val dbPath  = applicationContext.getDatabasePath("antique_collector_database").absolutePath
        Log.d("MainActivity", "Database path : $dbPath")
    }
}