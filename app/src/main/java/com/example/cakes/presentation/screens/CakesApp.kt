package com.example.cakes.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.cakes.ui.theme.CakeTheme

@Composable
fun CakeApp() {
    CakeTheme {
        CakeTopBar()
        Divider()
        Box(Modifier.size(16.dp)) { }
        CakeScreen()
    }
}