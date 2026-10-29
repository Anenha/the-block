package com.anenha.block.ui.util

import org.junit.Assert.assertEquals
import org.junit.Test

class FormatUtilsTest {

    @Test
    fun testFormatCurrencyWithDouble() {
        assertEquals("$0", formatCurrency(0.0))
        assertEquals("$500", formatCurrency(500.0))
        assertEquals("$1,420", formatCurrency(1420.0))
        assertEquals("$185,000", formatCurrency(185000.0))
        assertEquals("$1,250,000", formatCurrency(1250000.0))
    }

    @Test
    fun testFormatCurrencyWithLong() {
        assertEquals("$0", formatCurrency(0L))
        assertEquals("$200,000", formatCurrency(200000L))
    }

    @Test
    fun testFormatNumberWithInt() {
        assertEquals("0", formatNumber(0))
        assertEquals("420", formatNumber(420))
        assertEquals("1,420", formatNumber(1420))
        assertEquals("17,420", formatNumber(17420))
        assertEquals("126,793", formatNumber(126793))
    }

    @Test
    fun testFormatNumberWithLong() {
        assertEquals("1,000,000", formatNumber(1000000L))
    }

    @Test
    fun testFormatAuctionSchedule() {
        val (date, time) = formatAuctionSchedule("2026-04-05T19:00:00")
        assertEquals("Apr 5, 2026", date)
        assertEquals("7:00 PM EST", time)
    }

    @Test
    fun testParseVehicleColor() {
        val blue = parseVehicleColor("Deep Crystal Blue")
        assertEquals(androidx.compose.ui.graphics.Color(0xFF0F3260), blue)

        val grey = parseVehicleColor("Light Grey")
        assertEquals(androidx.compose.ui.graphics.Color(0xFFD3D6DC), grey)
    }
}
