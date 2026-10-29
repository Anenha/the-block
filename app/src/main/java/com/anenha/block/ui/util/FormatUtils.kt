package com.anenha.block.ui.util

import java.util.Locale

/**
 * Formats a numeric currency amount into a standard localized currency string (e.g., "$185,000").
 */
fun formatCurrency(amount: Double): String {
    return "$%,d".format(Locale.US, amount.toLong())
}

/**
 * Formats a whole-number currency amount into a standard localized currency string (e.g., "$185,000").
 */
fun formatCurrency(amount: Long): String {
    return "$%,d".format(Locale.US, amount)
}

/**
 * Formats an integer into a comma-separated localized number string (e.g., "17,420").
 */
fun formatNumber(number: Int): String {
    return "%,d".format(Locale.US, number)
}

/**
 * Formats a long number into a comma-separated localized number string (e.g., "17,420").
 */
fun formatNumber(number: Long): String {
    return "%,d".format(Locale.US, number)
}

/**
 * Parses an ISO date string (e.g., "2026-04-05T19:00:00") and returns formatted date and time strings.
 * Example: ("Apr 5, 2026", "10:00 AM EST")
 */
fun formatAuctionSchedule(isoString: String): Pair<String, String> {
    return try {
        val cleanIso = if (isoString.endsWith("Z")) isoString.dropLast(1) else isoString
        val dateTime = java.time.LocalDateTime.parse(cleanIso)
        val dateFormatter = java.time.format.DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.US)
        val timeFormatter = java.time.format.DateTimeFormatter.ofPattern("h:mm a", Locale.US)
        val dateStr = dateTime.format(dateFormatter)
        val timeStr = "${dateTime.format(timeFormatter)} EST"
        Pair(dateStr, timeStr)
    } catch (_: Exception) {
        Pair("Apr 5, 2026", "10:00 AM EST")
    }
}

/**
 * Maps vehicle color descriptions to representative UI swatch colors.
 */
fun parseVehicleColor(colorName: String): androidx.compose.ui.graphics.Color {
    val normalized = colorName.lowercase().trim()
    return when {
        normalized.contains("crystal blue") || normalized.contains("dark blue") -> androidx.compose.ui.graphics.Color(0xFF0F3260)
        normalized.contains("blue") -> androidx.compose.ui.graphics.Color(0xFF1D5AAB)
        normalized.contains("rapid red") || normalized.contains("dark red") -> androidx.compose.ui.graphics.Color(0xFF8B1E1E)
        normalized.contains("red") -> androidx.compose.ui.graphics.Color(0xFFC52828)
        normalized.contains("light grey") || normalized.contains("light gray") -> androidx.compose.ui.graphics.Color(0xFFD3D6DC)
        normalized.contains("dark grey") || normalized.contains("dark gray") -> androidx.compose.ui.graphics.Color(0xFF4A4E57)
        normalized.contains("grey") || normalized.contains("gray") -> androidx.compose.ui.graphics.Color(0xFF888E99)
        normalized.contains("silver") -> androidx.compose.ui.graphics.Color(0xFFB8BCC4)
        normalized.contains("white") -> androidx.compose.ui.graphics.Color(0xFFF0F2F5)
        normalized.contains("black") -> androidx.compose.ui.graphics.Color(0xFF17191C)
        normalized.contains("green") -> androidx.compose.ui.graphics.Color(0xFF265C38)
        normalized.contains("yellow") || normalized.contains("gold") -> androidx.compose.ui.graphics.Color(0xFFC79228)
        normalized.contains("orange") -> androidx.compose.ui.graphics.Color(0xFFD66011)
        else -> androidx.compose.ui.graphics.Color(0xFF64748B)
    }
}
