package com.example.cakes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.cakes.presentation.screens.CakeApp
import com.example.cakes.ui.theme.CakeTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * The main entry point for the application.
 * Annotated with [AndroidEntryPoint] to allow Hilt to inject dependencies into this activity.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Enables edge-to-edge support for modern Android devices.
        enableEdgeToEdge()
        
        setContent {
            // Apply the custom application theme.
            CakeTheme {
                // Initialize the main Composable entry point of the app.
                CakeApp()
            }
        }
    }
}
