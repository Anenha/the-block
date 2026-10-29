package com.anenha.block.data.datasource

import com.anenha.block.data.dto.VehicleDto
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class VehicleJsonParsingTest {

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        coerceInputValues = true
    }

    @Test
    fun `decodeFromString parses vehicle json accurately with all properties`() {
        val jsonPayload = """
        [
          {
            "id": "3cc3b89e-68b0-479e-af39-bca6251ea0b4",
            "vin": "TRD7L1KS0HNB5X3K3",
            "year": 2023,
            "make": "Ford",
            "model": "Bronco",
            "trim": "Big Bend",
            "body_style": "SUV",
            "exterior_color": "Burgundy",
            "interior_color": "Beige",
            "engine": "2.7L EcoBoost V6",
            "transmission": "automatic",
            "drivetrain": "4WD",
            "odometer_km": 47731,
            "fuel_type": "gasoline",
            "condition_grade": 3.8,
            "condition_report": "Average condition.",
            "damage_notes": [
              "Scratch on liftgate",
              "Minor rust on wheel wells"
            ],
            "title_status": "clean",
            "province": "Ontario",
            "city": "Toronto",
            "auction_start": "2026-04-05T14:00:00",
            "starting_bid": 14500,
            "reserve_price": 25000,
            "buy_now_price": null,
            "images": [
              "https://placehold.co/800x600?text=2023+Ford+Bronco+Photo+1"
            ],
            "selling_dealership": "King City Auto",
            "lot": "A-0043",
            "current_bid": 22800,
            "bid_count": 16
          }
        ]
        """.trimIndent()

        val parsed = json.decodeFromString<List<VehicleDto>>(jsonPayload)

        assertEquals(1, parsed.size)
        val dto = parsed.first()
        assertEquals("3cc3b89e-68b0-479e-af39-bca6251ea0b4", dto.id)
        assertEquals("TRD7L1KS0HNB5X3K3", dto.vin)
        assertEquals(2023, dto.year)
        assertEquals("Ford", dto.make)
        assertEquals("Bronco", dto.model)
        assertEquals("Big Bend", dto.trim)
        assertEquals(47731, dto.odometerKm)
        assertEquals(3.8, dto.conditionGrade, 0.0)
        assertEquals(2, dto.damageNotes.size)
        assertEquals(25000.0, dto.reservePrice)
        assertNull(dto.buyNowPrice)
        assertEquals(22800.0, dto.currentBid)
        assertEquals(16, dto.bidCount)
    }

    @Test
    fun `decodeFromString handles missing optional fields with defaults`() {
        val minimalJson = """
        [
          {
            "id": "min-1",
            "vin": "VINMIN1",
            "year": 2022,
            "make": "Mazda",
            "model": "CX-5",
            "body_style": "SUV",
            "exterior_color": "Soul Red",
            "interior_color": "Black",
            "engine": "2.5L",
            "transmission": "auto",
            "drivetrain": "AWD",
            "odometer_km": 30000,
            "fuel_type": "gasoline",
            "condition_grade": 4.0,
            "condition_report": "Good",
            "title_status": "clean",
            "province": "Quebec",
            "city": "Montreal",
            "auction_start": "2026-05-01T10:00:00",
            "starting_bid": 10000,
            "selling_dealership": "Montreal Mazda",
            "lot": "M-10",
            "current_bid": null,
            "bid_count": 0
          }
        ]
        """.trimIndent()

        val parsed = json.decodeFromString<List<VehicleDto>>(minimalJson)
        assertEquals(1, parsed.size)
        val dto = parsed.first()
        assertNull(dto.trim)
        assertNull(dto.reservePrice)
        assertNull(dto.buyNowPrice)
        assertNull(dto.currentBid)
        assertTrue(dto.damageNotes.isEmpty())
        assertTrue(dto.images.isEmpty())
    }
}
