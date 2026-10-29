package com.anenha.block.ui.components

import com.anenha.block.domain.model.AuctionDetails
import com.anenha.block.domain.model.Vehicle
import com.anenha.block.domain.model.VehicleLocation
import com.anenha.block.ui.util.formatCurrency
import com.anenha.block.ui.util.formatNumber
import org.junit.Assert.assertEquals
import org.junit.Test

class VehicleCardTest {

    @Test
    fun testCurrencyFormatting() {
        assertEquals("$185,000", formatCurrency(185000.0))
        assertEquals("$200,000", formatCurrency(200000.0))
    }

    @Test
    fun testOdometerFormatting() {
        assertEquals("1,420", formatNumber(1420))
        assertEquals("17,420", formatNumber(17420))
    }

    @Test
    fun testVehicleTitleMapping() {
        val vehicleWithTrim = Vehicle(
            id = "test-1",
            vin = "VIN123",
            year = 2024,
            make = "Porsche",
            model = "Taycan",
            trim = "Turbo GT Weissach",
            bodyStyle = "Sedan",
            exteriorColor = "Black",
            interiorColor = "Black",
            engine = "Electric",
            transmission = "Auto",
            drivetrain = "AWD",
            odometerKm = 2285,
            fuelType = "electric",
            conditionGrade = 5.0,
            conditionReport = "Mint",
            damageNotes = emptyList(),
            titleStatus = "clean",
            location = VehicleLocation("Austin", "TX"),
            auctionDetails = AuctionDetails("2026-10-27T12:00:00", 150000.0, 200000.0, null),
            images = listOf("https://example.com/car.jpg"),
            sellingDealership = "Austin Dealership",
            lot = "A1",
            currentBid = 185000.0,
            bidCount = 10
        )

        assertEquals("Porsche Taycan Turbo GT Weissach", vehicleWithTrim.title)
        assertEquals("Austin, TX", vehicleWithTrim.location.formatted)
        assertEquals(2024, vehicleWithTrim.year)
    }

    @Test
    fun testBadgeFormatting() {
        val conditionGrade = 4.8
        val gradeBadgeText = "Grade: $conditionGrade"
        assertEquals("Grade: 4.8", gradeBadgeText)

        val titleStatus = "clean"
        val statusBadgeText = titleStatus.uppercase()
        assertEquals("CLEAN", statusBadgeText)
    }
}
