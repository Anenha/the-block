package com.anenha.block.ui.theme

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val Shapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),  // rounded.sm (0.25rem / 4dp)
    small = RoundedCornerShape(8.dp),       // rounded.DEFAULT (0.5rem / 8dp)
    medium = RoundedCornerShape(12.dp),     // rounded.md (0.75rem / 12dp)
    large = RoundedCornerShape(16.dp),      // rounded.lg (1rem / 16dp)
    extraLarge = RoundedCornerShape(24.dp)  // rounded.xl (1.5rem / 24dp)
)

val FullShape = CircleShape // rounded.full (9999px)
