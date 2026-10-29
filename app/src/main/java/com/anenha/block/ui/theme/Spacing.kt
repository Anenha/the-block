package com.anenha.block.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class BlockSpacing(
    val spaceXs: Dp = 4.dp,          // 0.25rem (4dp)
    val spaceSm: Dp = 8.dp,          // 0.5rem (8dp)
    val spaceMd: Dp = 16.dp,         // 1rem (16dp)
    val spaceLg: Dp = 24.dp,         // 1.5rem (24dp)
    val spaceXl: Dp = 32.dp,         // 2rem (32dp)
    val gutter: Dp = 16.dp,          // 1rem (16dp)
    val gutterMobile: Dp = 12.dp,    // 0.75rem (12dp)
    val margin: Dp = 24.dp,          // 1.5rem (24dp)
    val marginMobile: Dp = 16.dp     // 1rem (16dp)
)

val LocalBlockSpacing = staticCompositionLocalOf { BlockSpacing() }
