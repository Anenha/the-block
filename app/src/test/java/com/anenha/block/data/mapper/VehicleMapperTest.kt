package com.anenha.block.data.mapper

import com.anenha.block.data.dto.VehicleDto
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class VehicleMapperTest {

    private fun createDto(
        trim: String? = "Big Bend",
        reservePrice: Double? = 25000.0,
        buyNowPrice: Double? = 30000.0,
        currentBid: Double? = 22000.0,
        damageNotes: List<String> = listOf("Scratch on bumper")
    ) = VehicleDto(
        id = "test-id-123",
        vin = "TRD7L1KS0HNB5X3K3",
        year = 2023,
        make = "Ford",
        model = "Bronco",
        trim = trim,
        bodyStyle = "SUV",
        exteriorColor = "Burgundy",
        interiorColor = "Beige",
        engine = "2.7L EcoBoost V6",
        transmission = "automatic",
        drivetrain = "4WD",
        odometerKm = 47731,
        fuelType = "gasoline",
        conditionGrade = 3.8,
        conditionReport = "Average condition.",
        damageNotes = damageNotes,
        titleStatus = "clean",
        province = "Ontario",
        city = "Toronto",
        auctionStart = "2026-04-05T14:00:00",
        startingBid = 14500.0,
        reservePrice = reservePrice,
        buyNowPrice = buyNowPrice,
        images = listOf("https://example.com/photo1.jpg"),
        sellingDealership = "King City Auto",
        lot = "A-0043",
        currentBid = currentBid,
        bidCount = 16
    )

    @Test
    fun `toDomain accurately maps all fields from complete VehicleDto`() {
        val dto = createDto()
        val vehicle = dto.toDomain()

        assertEquals("test-id-123", vehicle.id)
        assertEquals("TRD7L1KS0HNB5X3K3", vehicle.vin)
        assertEquals(2023, vehicle.year)
        assertEquals("Ford", vehicle.make)
        assertEquals("Bronco", vehicle.model)
        assertEquals("Big Bend", vehicle.trim)
        assertEquals("SUV", vehicle.bodyStyle)
        assertEquals("Burgundy", vehicle.exteriorColor)
        assertEquals("Beige", vehicle.interiorColor)
        assertEquals("2.7L EcoBoost V6", vehicle.engine)
        assertEquals("automatic", vehicle.transmission)
        assertEquals("4WD", vehicle.drivetrain)
        assertEquals(47731, vehicle.odometerKm)
        assertEquals("gasoline", vehicle.fuelType)
        assertEquals(3.8, vehicle.conditionGrade, 0.0)
        assertEquals("Average condition.", vehicle.conditionReport)
        assertEquals(listOf("Scratch on bumper"), vehicle.damageNotes)
        assertEquals("clean", vehicle.titleStatus)
        assertEquals("Toronto", vehicle.location.city)
        assertEquals("Ontario", vehicle.location.province)
        assertEquals("Toronto, Ontario", vehicle.location.formatted)
        assertEquals("2026-04-05T14:00:00", vehicle.auctionDetails.auctionStartIso)
        assertEquals(14500.0, vehicle.auctionDetails.startingBid, 0.0)
        assertEquals(25000.0, vehicle.auctionDetails.reservePrice)
        assertEquals(30000.0, vehicle.auctionDetails.buyNowPrice)
        assertEquals(listOf("https://example.com/photo1.jpg"), vehicle.images)
        assertEquals("King City Auto", vehicle.sellingDealership)
        assertEquals("A-0043", vehicle.lot)
        assertEquals(22000.0, vehicle.currentBid)
        assertEquals(16, vehicle.bidCount)
    }

    @Test
    fun `toDomain handles nullable fields correctly`() {
        val dto = createDto(
            trim = null,
            reservePrice = null,
            buyNowPrice = null,
            currentBid = null,
            damageNotes = emptyList()
        )
        val vehicle = dto.toDomain()

        assertNull(vehicle.trim)
        assertNull(vehicle.auctionDetails.reservePrice)
        assertNull(vehicle.auctionDetails.buyNowPrice)
        assertNull(vehicle.currentBid)
        assertTrue(vehicle.damageNotes.isEmpty())
    }

    @Test
    fun `title property formats with and without trim`() {
        val withTrim = createDto(trim = "Badlands").toDomain()
        assertEquals("Ford Bronco Badlands", withTrim.title)

        val withoutTrim = createDto(trim = null).toDomain()
        assertEquals("Ford Bronco", withoutTrim.title)

        val withBlankTrim = createDto(trim = "   ").toDomain()
        assertEquals("Ford Bronco", withBlankTrim.title)
    }

    @Test
    fun `isReserveMet calculates reserve threshold accurately`() {
        val withReserve = createDto(reservePrice = 20000.0).toDomain()
        assertTrue(withReserve.auctionDetails.isReserveMet(20000.0))
        assertTrue(withReserve.auctionDetails.isReserveMet(25000.0))
        assertFalse(withReserve.auctionDetails.isReserveMet(19999.0))

        val noReserve = createDto(reservePrice = null).toDomain()
        assertTrue(noReserve.auctionDetails.isReserveMet(5000.0))
    }
}
