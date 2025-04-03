package com.example.antiquecollector.ui.screens.settings

import android.Manifest
import android.app.Activity
import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.antiquecollector.R
import com.example.antiquecollector.ui.screens.settings.helper.NotificationHelper
import com.example.antiquecollector.ui.theme.AntiqueCollectorTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateUp: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showCurrencyPicker by remember { mutableStateOf(false) }
    var showLocationPicker by remember { mutableStateOf(false) }
    val context = LocalContext.current

    var showExactAlarmRationale by remember { mutableStateOf(false) }


    // Notification permission launcher
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.updatePushNotifications(true)
        }
    }

    // Exact Alarm Permission launcher (launches system settings)
    val exactAlarmPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { _ ->
        // Check the permission again after returning from system settings.
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.SCHEDULE_EXACT_ALARM
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // If granted, schedule the notification:
            NotificationHelper.scheduleDailyNotification(context)
        } else {
            // Handle the case where the user still didn't grant permission in system settings.
            //  e.g., Show a message that notifications won't work without it
            println("SCHEDULE_EXACT_ALARM permission was not granted in settings.")
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFF9F3E8), // Cream background
                    titleContentColor = Color.Black
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF9F3E8)) // Cream background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                // Notifications section
                SectionHeader(title = "Notifications")


                SwitchPreference(
                    icon = R.drawable.ic_notification,
                    title = "Push Notifications",
                    checked = uiState.pushNotifications,
                    onCheckedChange = { enabled ->
                        if (enabled) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                // Handle POST_NOTIFICATIONS for Android 13+
                                if (ContextCompat.checkSelfPermission(
                                        context,
                                        Manifest.permission.POST_NOTIFICATIONS
                                    ) != PackageManager.PERMISSION_GRANTED
                                ) {
                                    notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                                    return@SwitchPreference // Exit early: push notification enable will happen in the callback.
                                }
                            }

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                                val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                                if (!alarmManager.canScheduleExactAlarms()) {
                                    // Need to request SCHEDULE_EXACT_ALARM
                                    // Show rationale if needed:
                                    if (shouldShowRequestPermissionRationale(
                                            context as Activity,
                                            Manifest.permission.SCHEDULE_EXACT_ALARM
                                        )
                                    ) {
                                        showExactAlarmRationale = true
                                    } else {
                                        // If no rationale needed or user already saw it, go directly to settings:
                                        launchExactAlarmPermissionSettings(context, exactAlarmPermissionLauncher)
                                    }
                                    return@SwitchPreference
                                }
                            }
                            // Either permission already granted, or below Android 12, so enable notifications & schedule immediately:
                            viewModel.updatePushNotifications(enabled) // update view model and ui state
                            NotificationHelper.scheduleDailyNotification(context) // schedule daily notifications

                        } else {
                            // User disabled notifications, cancel the scheduled notification:
                            viewModel.updatePushNotifications(enabled)
                            NotificationHelper.cancelScheduledNotification(context)
                        }
                    },
                    enabled = true
                )

                SwitchPreference(
                    icon = R.drawable.ic_notification,
                    title = "Email Notifications",
                    subtitle = "coming soon",
                    checked = uiState.emailNotifications,
                    onCheckedChange = viewModel::updateEmailNotifications,
                    enabled = false
                )

                SwitchPreference(
                    icon = R.drawable.ic_notification,
                    title = "Sound Alerts",
                    subtitle = "coming soon",
                    checked = uiState.soundAlerts,
                    onCheckedChange = viewModel::updateSoundAlerts,
                    enabled = false
                )

                HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))

                // Currency section
                SectionHeader(title = "Currency")

                DropdownPreference(
                    icon = R.drawable.ic_currency,
                    title = "USD - US Dollar",
                    onClick = { showCurrencyPicker = true }
                )

                Text(
                    text = "Currency will be applied to all transactions.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                )

                HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))

                // Location section
                SectionHeader(title = "Location")

                PreferenceItem(
                    icon = R.drawable.ic_location,
                    title = "Current Location",
                    subtitle = uiState.location
                )

                Button(
                    onClick = { showLocationPicker = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = Color.White
                    )
                ) {
                    Text("Change Location")
                }

                HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))

                // Appearance section
                SectionHeader(title = "Appearance")

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Light mode option
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { viewModel.updateDarkMode(false) }
                    ) {
                        RadioButton(
                            selected = !uiState.darkMode,
                            onClick = { viewModel.updateDarkMode(false) },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = MaterialTheme.colorScheme.primary
                            )
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.LightMode,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Light Mode",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }

                    // Dark mode option
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { viewModel.updateDarkMode(true) }
                    ) {
                        RadioButton(
                            selected = uiState.darkMode,
                            onClick = { viewModel.updateDarkMode(true) },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = MaterialTheme.colorScheme.primary
                            )
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.DarkMode,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Dark Mode",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }

                HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))

                // Backup & Sync section
                SectionHeader(title = "Backup & Sync")

                SwitchPreference(
                    icon = R.drawable.ic_backup,
                    title = "Auto Backup",
                    subtitle = "Coming soon",
                    checked = uiState.autoBackup,
                    onCheckedChange = viewModel::updateAutoBackup,
                    enabled = false
                )

                if (uiState.lastBackup != null) {
                    Text(
                        text = "Last backup: ${uiState.lastBackup}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                    )
                }

                OutlinedButton(
                    onClick = { viewModel.performBackup() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.primary
                    ),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        brush = SolidColor(MaterialTheme.colorScheme.primary)
                    )
                ) {
                    Text("Backup Now")
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Show currency picker dialog
            if (showCurrencyPicker) {
                CurrencyPickerDialog(
                    selectedCurrency = uiState.currency,
                    onCurrencySelected = {
                        viewModel.updateCurrency(it)
                        showCurrencyPicker = false
                    },
                    onDismiss = { showCurrencyPicker = false }
                )
            }

            // Show location picker dialog
            if (showLocationPicker) {
                LocationPickerDialog(
                    currentLocation = uiState.location,
                    onLocationSelected = {
                        viewModel.updateLocation(it)
                        showLocationPicker = false
                    },
                    onDismiss = { showLocationPicker = false }
                )
            }

            if (showExactAlarmRationale) {
                AlertDialog(
                    onDismissRequest = { showExactAlarmRationale = false },
                    title = { Text("Allow Exact Alarms") },
                    text = { Text("This app needs permission to schedule exact daily notifications. Please allow this in settings.") },
                    confirmButton = {
                        TextButton(onClick = {
                            showExactAlarmRationale = false
                            launchExactAlarmPermissionSettings(context, exactAlarmPermissionLauncher)
                        }) {
                            Text("Go to Settings")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showExactAlarmRationale = false }) {
                            Text("Cancel")
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.primary,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
    )
}

@Composable
fun SwitchPreference(
    icon: Int,
    title: String,
    subtitle: String? = null,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    enabled: Boolean = true
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = if (enabled) Color.Unspecified else Color.Gray
            )

            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFFF44336) // Red color for "Coming Soon"
                )
            }
        }

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            enabled = enabled,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = MaterialTheme.colorScheme.primary,
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color.Gray.copy(alpha = 0.5f),
                disabledCheckedThumbColor = Color.White,
                disabledCheckedTrackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                disabledUncheckedThumbColor = Color.White,
                disabledUncheckedTrackColor = Color.Gray.copy(alpha = 0.3f)
            )
        )
    }
}

@Composable
fun PreferenceItem(
    icon: Int,
    title: String,
    subtitle: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge
            )

            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun DropdownPreference(
    icon: Int,
    title: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )

        Icon(
            imageVector = Icons.Default.KeyboardArrowDown,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun CurrencyPickerDialog(
    selectedCurrency: String,
    onCurrencySelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val currencies = listOf(
        "USD - US Dollar",
        "EUR - Euro",
        "GBP - British Pound",
        "JPY - Japanese Yen",
        "CAD - Canadian Dollar",
        "AUD - Australian Dollar"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select Currency") },
        text = {
            Column {
                currencies.forEach { currency ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onCurrencySelected(currency) }
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = currency == selectedCurrency,
                            onClick = { onCurrencySelected(currency) },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = MaterialTheme.colorScheme.primary
                            )
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(text = currency)
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = MaterialTheme.colorScheme.primary)
            }
        }
    )
}

@Composable
fun LocationPickerDialog(
    currentLocation: String,
    onLocationSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val locations = listOf(
        "New York, USA",
        "London, UK",
        "Paris, France",
        "Tokyo, Japan",
        "Sydney, Australia",
        "Toronto, Canada",
        "Berlin, Germany",
        "Rome, Italy"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select Location") },
        text = {
            Column {
                locations.forEach { location ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onLocationSelected(location) }
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = location == currentLocation,
                            onClick = { onLocationSelected(location) },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = MaterialTheme.colorScheme.primary
                            )
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(text = location)
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = MaterialTheme.colorScheme.primary)
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun SettingsScreenPrev() {
    AntiqueCollectorTheme {
        SettingsScreen(
            onNavigateUp = {}
        )
    }
}

private fun launchExactAlarmPermissionSettings(
    context: Context,
    launcher: ActivityResultLauncher<Intent>
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
            data = Uri.fromParts("package", context.packageName, null)
        }
        launcher.launch(intent)
    }
}