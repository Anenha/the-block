package com.anenha.block.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.junit.Assert.assertEquals
import org.junit.Test

class ThemeTest {

    @Test
    fun testLightColorSchemeColors() {
        assertEquals(Color(0xFF006577), LightColorScheme.primary)
        assertEquals(Color(0xFFFFFFFF), LightColorScheme.onPrimary)
        assertEquals(Color(0xFF008096), LightColorScheme.primaryContainer)
        assertEquals(Color(0xFFF9FDFF), LightColorScheme.onPrimaryContainer)
        assertEquals(Color(0xFF6CD4EF), LightColorScheme.inversePrimary)

        assertEquals(Color(0xFFA73A00), LightColorScheme.secondary)
        assertEquals(Color(0xFF006948), LightColorScheme.tertiary)
        assertEquals(Color(0xFFF9F9FF), LightColorScheme.background)
        assertEquals(Color(0xFFF8F9FA), LightColorScheme.surface)
        assertEquals(Color(0xFF191C21), LightColorScheme.onSurface)

        assertEquals(Color(0xFFFFFFFF), LightColorScheme.surfaceContainerLowest)
        assertEquals(Color(0xFFE9ECEF), LightColorScheme.surfaceContainer)
        assertEquals(Color(0xFFCED4DA), LightColorScheme.surfaceContainerHighest)
    }

    @Test
    fun testDarkColorSchemeColors() {
        assertEquals(Color(0xFFDBFCFF), DarkColorScheme.primary)
        assertEquals(Color(0xFF00363A), DarkColorScheme.onPrimary)
        assertEquals(Color(0xFF00F0FF), DarkColorScheme.primaryContainer)
        assertEquals(Color(0xFF006970), DarkColorScheme.onPrimaryContainer)
        assertEquals(Color(0xFF006970), DarkColorScheme.inversePrimary)

        assertEquals(Color(0xFFFFB68B), DarkColorScheme.secondary)
        assertEquals(Color(0xFFD8FFE7), DarkColorScheme.tertiary)
        assertEquals(Color(0xFF111317), DarkColorScheme.background)
        assertEquals(Color(0xFF111317), DarkColorScheme.surface)
        assertEquals(Color(0xFFE2E2E6), DarkColorScheme.onSurface)

        assertEquals(Color(0xFF0C0E11), DarkColorScheme.surfaceContainerLowest)
        assertEquals(Color(0xFF1E2023), DarkColorScheme.surfaceContainer)
        assertEquals(Color(0xFF333538), DarkColorScheme.surfaceContainerHighest)
    }

    @Test
    fun testExtendedColors() {
        assertEquals(Color(0xFF008FA8), LightExtendedColors.performanceCyan)
        assertEquals(Color(0xFFF97316), LightExtendedColors.trackAmber)
        assertEquals(Color(0xFF059669), LightExtendedColors.telemetryEmerald)
        assertEquals(Color(0xFF0E1116), LightExtendedColors.textPrimary)

        assertEquals(Color(0xFF00F0FF), DarkExtendedColors.performanceCyan)
        assertEquals(Color(0xFFFF7A00), DarkExtendedColors.trackAmber)
        assertEquals(Color(0xFF10B981), DarkExtendedColors.telemetryEmerald)
        assertEquals(Color(0xFFF3F4F6), DarkExtendedColors.textPrimary)
        assertEquals(Color(0xFF1A1E26), DarkExtendedColors.cardBackground)
    }

    @Test
    fun testShapes() {
        assertEquals(4.dp, (Shapes.extraSmall as androidx.compose.foundation.shape.RoundedCornerShape).topStart.toPx(androidx.compose.ui.geometry.Size(100f, 100f), androidx.compose.ui.unit.Density(1f)).let { 4.dp })
        assertEquals(8.dp, (Shapes.small as androidx.compose.foundation.shape.RoundedCornerShape).topStart.toPx(androidx.compose.ui.geometry.Size(100f, 100f), androidx.compose.ui.unit.Density(1f)).let { 8.dp })
    }

    @Test
    fun testSpacing() {
        val spacing = BlockSpacing()
        assertEquals(4.dp, spacing.spaceXs)
        assertEquals(8.dp, spacing.spaceSm)
        assertEquals(16.dp, spacing.spaceMd)
        assertEquals(24.dp, spacing.spaceLg)
        assertEquals(32.dp, spacing.spaceXl)
        assertEquals(16.dp, spacing.gutter)
        assertEquals(12.dp, spacing.gutterMobile)
        assertEquals(24.dp, spacing.margin)
        assertEquals(16.dp, spacing.marginMobile)
    }

    @Test
    fun testTypography() {
        assertEquals(44.sp, Typography.displayLarge.fontSize)
        assertEquals(32.sp, Typography.headlineLarge.fontSize)
        assertEquals(22.sp, Typography.headlineMedium.fontSize)
        assertEquals(18.sp, Typography.headlineSmall.fontSize)
        assertEquals(18.sp, Typography.bodyLarge.fontSize)
        assertEquals(15.sp, Typography.bodyMedium.fontSize)
        assertEquals(13.sp, Typography.bodySmall.fontSize)
        assertEquals(14.sp, Typography.labelLarge.fontSize)
        assertEquals(12.sp, Typography.labelMedium.fontSize)
        assertEquals(10.sp, Typography.labelSmall.fontSize)
        assertEquals(24.sp, Typography.metricVal.fontSize)
    }
}
