package com.example.cakes.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

/**
 * Custom shape definitions for the application's UI components.
 * Follows Material 3 shape guidelines with adjusted corner radii.
 */
val CakeShapes = Shapes(

    extraSmall = RoundedCornerShape(8.dp),

    small = RoundedCornerShape(12.dp),

    medium = RoundedCornerShape(16.dp),

    large = RoundedCornerShape(24.dp),

    extraLarge = RoundedCornerShape(32.dp)
)
