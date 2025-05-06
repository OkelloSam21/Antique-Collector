package com.example.antiquecollector.ui.screens.settings

import android.annotation.SuppressLint
import android.content.Context
import androidx.core.content.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.antiquecollector.domain.model.UserPreference
import com.example.antiquecollector.domain.repository.PreferenceRepository
import com.example.antiquecollector.ui.screens.settings.helper.NotificationHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferenceRepository: PreferenceRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        loadSettings()
    }

    private fun loadSettings() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true) }
                
                // Get preferences
                val pushNotifications = preferenceRepository.getPreferenceValue("notification_push", "true") == "true"
                val emailNotifications = preferenceRepository.getPreferenceValue("notification_email", "false") == "true"
                val soundAlerts = preferenceRepository.getPreferenceValue("notification_sound", "true") == "true"
                val currency = preferenceRepository.getPreferenceValue("currency", "USD")
                val location = preferenceRepository.getPreferenceValue("location", "New York, USA")
                val darkMode = preferenceRepository.getPreferenceValue("dark_mode", "false") == "true"
                val autoBackup = preferenceRepository.getPreferenceValue("auto_backup", "true") == "true"
                val lastBackup = preferenceRepository.getPreferenceValue("last_backup", "")
                
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        pushNotifications = pushNotifications,
                        emailNotifications = emailNotifications,
                        soundAlerts = soundAlerts,
                        currency = currency,
                        location = location,
                        darkMode = darkMode,
                        autoBackup = autoBackup,
                        lastBackup = lastBackup.ifBlank { null }
                    ) 
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    private fun saveNotificationEnabledState(enabled: Boolean) {
        context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE).edit { // Replace "app_prefs" with your actual SharedPreferences name
            putBoolean("notification_enabled", enabled)
            apply()
        }
    }

    @SuppressLint("ScheduleExactAlarm")
    fun updatePushNotifications(enabled: Boolean) {
        viewModelScope.launch {
            preferenceRepository.setPreference("notification_push", enabled.toString())
            _uiState.update { it.copy(pushNotifications = enabled) }
            saveNotificationEnabledState(enabled)

            // Use the unified notification system
            if (enabled) {
                NotificationHelper.scheduleDailyNotification(context)
            } else {
                NotificationHelper.cancelScheduledNotification(context)
            }
        }
    }

    fun updateEmailNotifications(enabled: Boolean) {
        viewModelScope.launch {
            preferenceRepository.setPreference("notification_email", enabled.toString())
            _uiState.update { it.copy(emailNotifications = enabled) }
        }
    }

    fun updateSoundAlerts(enabled: Boolean) {
        viewModelScope.launch {
            preferenceRepository.setPreference("notification_sound", enabled.toString())
            _uiState.update { it.copy(soundAlerts = enabled) }
        }
    }

    fun updateCurrency(currency: String) {
        viewModelScope.launch {
            preferenceRepository.setPreference("currency", currency)
            _uiState.update { it.copy(currency = currency) }
        }
    }

    fun updateLocation(location: String) {
        viewModelScope.launch {
            preferenceRepository.setPreference("location", location)
            _uiState.update { it.copy(location = location) }
        }
    }

    fun updateDarkMode(enabled: Boolean) {
        viewModelScope.launch {
            preferenceRepository.setPreference("dark_mode", enabled.toString())
            _uiState.update { it.copy(darkMode = enabled) }
        }
    }

    fun updateAutoBackup(enabled: Boolean) {
        viewModelScope.launch {
            preferenceRepository.setPreference("auto_backup", enabled.toString())
            _uiState.update { it.copy(autoBackup = enabled) }
        }
    }

    fun performBackup() {
        viewModelScope.launch {
            // Here you would implement the actual backup logic
            // For now we'll just update the last backup time
            val now = Date()
            val backupTime = "Today at " + now.hours.toString().padStart(2, '0') + ":" + 
                              now.minutes.toString().padStart(2, '0')
            
            preferenceRepository.setPreference("last_backup", backupTime)
            _uiState.update { it.copy(lastBackup = backupTime) }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}

data class SettingsUiState(
    val isLoading: Boolean = false,
    val pushNotifications: Boolean = true,
    val emailNotifications: Boolean = false,
    val soundAlerts: Boolean = true,
    val currency: String = "USD",
    val location: String = "New York, USA",
    val darkMode: Boolean = false,
    val autoBackup: Boolean = true,
    val lastBackup: String? = null,
    val error: String? = null
)