package com.anenha.block.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

// Kinetic Apex Light Palette
val LightPrimary = Color(0xFF006577)
val LightOnPrimary = Color(0xFFFFFFFF)
val LightPrimaryContainer = Color(0xFF008096)
val LightOnPrimaryContainer = Color(0xFFF9FDFF)
val LightInversePrimary = Color(0xFF6CD4EF)

val LightSecondary = Color(0xFFA73A00)
val LightOnSecondary = Color(0xFFFFFFFF)
val LightSecondaryContainer = Color(0xFFFD651E)
val LightOnSecondaryContainer = Color(0xFF571A00)

val LightTertiary = Color(0xFF006948)
val LightOnTertiary = Color(0xFFFFFFFF)
val LightTertiaryContainer = Color(0xFF00855D)
val LightOnTertiaryContainer = Color(0xFFF5FFF7)

val LightError = Color(0xFFBA1A1A)
val LightOnError = Color(0xFFFFFFFF)
val LightErrorContainer = Color(0xFFFFDAD6)
val LightOnErrorContainer = Color(0xFF93000A)

val LightBackground = Color(0xFFF9F9FF)
val LightOnBackground = Color(0xFF191C21)
val LightSurface = Color(0xFFF8F9FA)
val LightOnSurface = Color(0xFF191C21)
val LightSurfaceVariant = Color(0xFFE1E2E9)
val LightOnSurfaceVariant = Color(0xFF3E494C)
val LightOutline = Color(0xFF6E797D)
val LightOutlineVariant = Color(0xFFBDC8CC)
val LightScrim = Color(0xFF000000)
val LightInverseSurface = Color(0xFF2E3036)
val LightInverseOnSurface = Color(0xFFEFF0F8)

val LightSurfaceDim = Color(0xFFD8DAE1)
val LightSurfaceBright = Color(0xFFF9F9FF)
val LightSurfaceContainerLowest = Color(0xFFFFFFFF)
val LightSurfaceContainerLow = Color(0xFFF1F3F5)
val LightSurfaceContainer = Color(0xFFE9ECEF)
val LightSurfaceContainerHigh = Color(0xFFDEE2E6)
val LightSurfaceContainerHighest = Color(0xFFCED4DA)
val LightSurfaceTint = Color(0xFF00687A)

// Fixed tokens (Light)
val LightPrimaryFixed = Color(0xFFADECFF)
val LightPrimaryFixedDim = Color(0xFF6CD4EF)
val LightOnPrimaryFixed = Color(0xFF001F26)
val LightOnPrimaryFixedVariant = Color(0xFF004E5D)
val LightSecondaryFixed = Color(0xFFFFDBCE)
val LightSecondaryFixedDim = Color(0xFFFFB599)
val LightOnSecondaryFixed = Color(0xFF370E00)
val LightOnSecondaryFixedVariant = Color(0xFF7F2B00)
val LightTertiaryFixed = Color(0xFF85F8C4)
val LightTertiaryFixedDim = Color(0xFF68DBA9)
val LightOnTertiaryFixed = Color(0xFF002114)
val LightOnTertiaryFixedVariant = Color(0xFF005137)

// Kinetic Apex Dark Palette
val DarkPrimary = Color(0xFFDBFCFF)
val DarkOnPrimary = Color(0xFF00363A)
val DarkPrimaryContainer = Color(0xFF00F0FF)
val DarkOnPrimaryContainer = Color(0xFF006970)
val DarkInversePrimary = Color(0xFF006970)

val DarkSecondary = Color(0xFFFFB68B)
val DarkOnSecondary = Color(0xFF522300)
val DarkSecondaryContainer = Color(0xFFFF7F1C)
val DarkOnSecondaryContainer = Color(0xFF602A00)

val DarkTertiary = Color(0xFFD8FFE7)
val DarkOnTertiary = Color(0xFF003824)
val DarkTertiaryContainer = Color(0xFF65F2B5)
val DarkOnTertiaryContainer = Color(0xFF006D4A)

val DarkError = Color(0xFFFFB4AB)
val DarkOnError = Color(0xFF690005)
val DarkErrorContainer = Color(0xFF93000A)
val DarkOnErrorContainer = Color(0xFFFFDAD6)

val DarkBackground = Color(0xFF111317)
val DarkOnBackground = Color(0xFFE2E2E6)
val DarkSurface = Color(0xFF111317)
val DarkOnSurface = Color(0xFFE2E2E6)
val DarkSurfaceVariant = Color(0xFF333538)
val DarkOnSurfaceVariant = Color(0xFFB9CACB)
val DarkOutline = Color(0xFF849495)
val DarkOutlineVariant = Color(0xFF3B494B)
val DarkScrim = Color(0xFF000000)
val DarkInverseSurface = Color(0xFFE2E2E6)
val DarkInverseOnSurface = Color(0xFF2F3034)

val DarkSurfaceDim = Color(0xFF111317)
val DarkSurfaceBright = Color(0xFF37393D)
val DarkSurfaceContainerLowest = Color(0xFF0C0E11)
val DarkSurfaceContainerLow = Color(0xFF1A1C1F)
val DarkSurfaceContainer = Color(0xFF1E2023)
val DarkSurfaceContainerHigh = Color(0xFF282A2D)
val DarkSurfaceContainerHighest = Color(0xFF333538)
val DarkSurfaceTint = Color(0xFF00DBE9)

// Fixed tokens (Dark)
val DarkPrimaryFixed = Color(0xFF7DF4FF)
val DarkPrimaryFixedDim = Color(0xFF00DBE9)
val DarkOnPrimaryFixed = Color(0xFF002022)
val DarkOnPrimaryFixedVariant = Color(0xFF004F54)
val DarkSecondaryFixed = Color(0xFFFFDBC8)
val DarkSecondaryFixedDim = Color(0xFFFFB68B)
val DarkOnSecondaryFixed = Color(0xFF321200)
val DarkOnSecondaryFixedVariant = Color(0xFF753400)
val DarkTertiaryFixed = Color(0xFF6FFBBE)
val DarkTertiaryFixedDim = Color(0xFF4EDEA3)
val DarkOnTertiaryFixed = Color(0xFF002113)
val DarkOnTertiaryFixedVariant = Color(0xFF005236)

// Extended Brand & Telemetry Tokens
@Immutable
data class BlockExtendedColors(
    val cyanCore: Color,
    val cyanDeep: Color,
    val cyanContainer: Color,
    val trackAmber: Color,
    val telemetryEmerald: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textMuted: Color,
    val borderSubtle: Color,
    val borderStrong: Color,
    val performanceCyan: Color,
    val cardBackground: Color,
    val glassBackground: Color,
    val glassBorder: Color
)

val LightExtendedColors = BlockExtendedColors(
    cyanCore = Color(0xFF00A3C4),
    cyanDeep = Color(0xFF006D80),
    cyanContainer = Color(0xFFE0F7FA),
    trackAmber = Color(0xFFF97316),
    telemetryEmerald = Color(0xFF059669),
    textPrimary = Color(0xFF0E1116),
    textSecondary = Color(0xFF4A5568),
    textMuted = Color(0xFF64748B),
    borderSubtle = Color(0x140E1116), // rgba(14, 17, 22, 0.08)
    borderStrong = Color(0x290E1116), // rgba(14, 17, 22, 0.16)
    performanceCyan = Color(0xFF008FA8),
    cardBackground = Color(0xFFFFFFFF),
    glassBackground = Color(0xE0FFFFFF), // rgba(255, 255, 255, 0.88)
    glassBorder = Color(0x0F0E1116)      // rgba(14, 17, 22, 0.06)
)

val DarkExtendedColors = BlockExtendedColors(
    cyanCore = Color(0xFF00F0FF),
    cyanDeep = Color(0xFF006970),
    cyanContainer = Color(0x2600F0FF), // rgba(0, 240, 255, 0.15)
    trackAmber = Color(0xFFFF7A00),
    telemetryEmerald = Color(0xFF10B981),
    textPrimary = Color(0xFFF3F4F6),
    textSecondary = Color(0xFF9CA3AF),
    textMuted = Color(0xFF6B7280),
    borderSubtle = Color(0x14FFFFFF), // rgba(255, 255, 255, 0.08)
    borderStrong = Color(0x24FFFFFF), // rgba(255, 255, 255, 0.14)
    performanceCyan = Color(0xFF00F0FF),
    cardBackground = Color(0xFF1A1E26),
    glassBackground = Color(0xB8161B22), // rgba(22, 27, 34, 0.72)
    glassBorder = Color(0x1FFFFFFF)      // rgba(255, 255, 255, 0.12)
)

val LocalBlockExtendedColors = staticCompositionLocalOf { LightExtendedColors }