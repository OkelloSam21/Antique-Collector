package com.example.antiquecollector

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.antiquecollector.ui.navigation.NavGraph
import com.example.antiquecollector.ui.screens.splash.SplashViewModel
import com.example.antiquecollector.ui.theme.AntiqueCollectorTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Main activity for the Antique Collector app.
 * Uses Jetpack Compose for the UI and Hilt for dependency injection.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        // Install splash screen before calling super.onCreate()
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        // Create the splash view model
        val splashViewModel = SplashViewModel()

        // Keep the splash screen on-screen until the app is fully initialized
        splashScreen.setKeepOnScreenCondition {
            !splashViewModel.isAppReady.value
        }

        // Initialize app resources in background
        lifecycleScope.launch {
            splashViewModel.initialize()
        }

        setContent {
            val isAppReady by splashViewModel.isAppReady.collectAsStateWithLifecycle()

            // Only render main content when app is ready
            if (isAppReady) {
                AntiqueCollectorTheme {
                    // A surface container using the 'background' color from the theme
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        // The navigation graph will handle routing to different screens
                        NavGraph()
                    }
                }
            }
        }
    }
}