package com.example.antiquecollector

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Application class for the Antique Collector app.
 * The HiltAndroidApp annotation triggers Hilt's code generation.
 */
@HiltAndroidApp
class AntiqueCollectorApp : Application()