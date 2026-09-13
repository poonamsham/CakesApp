package com.example.cakes

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Base Application class for the Cakes project.
 * Initialized with [HiltAndroidApp] to enable dependency injection throughout the application.
 */
@HiltAndroidApp
class CakeApplication : Application()
