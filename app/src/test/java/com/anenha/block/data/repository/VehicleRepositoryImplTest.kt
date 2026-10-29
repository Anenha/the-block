package com.anenha.block.data.repository

import app.cash.turbine.test
import com.anenha.block.data.datasource.VehicleAssetDataSource
import com.anenha.block.data.dto.VehicleDto
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class VehicleRepositoryImplTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    private fun createTestDto(
        id: String,
        currentBid: Double? = 20000.0,
        startingBid: Double = 15000.0,
        bidCount: Int = 5
    ) = VehicleDto(
        id = id,
        vin = "VIN-$id",
        year = 2023,
        make = "Ford",
        model = "Bronco",
        trim = "Big Bend",
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
        damageNotes = emptyList(),
        titleStatus = "clean",
        province = "Ontario",
        city = "Toronto",
        auctionStart = "2026-04-05T14:00:00",
        startingBid = startingBid,
        reservePrice = 25000.0,
        buyNowPrice = null,
        images = emptyList(),
        sellingDealership = "King City Auto",
        lot = "A-0043",
        currentBid = currentBid,
        bidCount = bidCount
    )

    private class FakeDataSource(val dtoList: List<VehicleDto>) : VehicleAssetDataSource {
        override suspend fun loadVehicles(): List<VehicleDto> = dtoList
    }

    @Test
    fun `placeBid succeeds and updates vehicle state reactively`() = runTest(testDispatcher) {
        val initialDto = createTestDto("v-1", currentBid = 20000.0, bidCount = 5)
        val repository = VehicleRepositoryImpl(FakeDataSource(listOf(initialDto)), testDispatcher)

        repository.getVehicleById("v-1").test {
            val initial = awaitItem()
            assertEquals(20000.0, initial?.currentBid)
            assertEquals(5, initial?.bidCount)

            // Place a higher bid
            val result = repository.placeBid("v-1", 21500.0)
            assertTrue(result.isSuccess)
            assertEquals(21500.0, result.getOrNull()?.currentBid)
            assertEquals(6, result.getOrNull()?.bidCount)

            // Reactive emission from Flow
            val updated = awaitItem()
            assertEquals(21500.0, updated?.currentBid)
            assertEquals(6, updated?.bidCount)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `placeBid rejects bid equal to or lower than current bid`() = runTest(testDispatcher) {
        val initialDto = createTestDto("v-1", currentBid = 20000.0, bidCount = 5)
        val repository = VehicleRepositoryImpl(FakeDataSource(listOf(initialDto)), testDispatcher)

        // Equal to current bid
        val resultEqual = repository.placeBid("v-1", 20000.0)
        assertTrue(resultEqual.isFailure)

        // Lower than current bid
        val resultLower = repository.placeBid("v-1", 19999.0)
        assertTrue(resultLower.isFailure)
    }

    @Test
    fun `placeBid fails when vehicle ID does not exist`() = runTest(testDispatcher) {
        val initialDto = createTestDto("v-1")
        val repository = VehicleRepositoryImpl(FakeDataSource(listOf(initialDto)), testDispatcher)

        val result = repository.placeBid("non-existent-id", 30000.0)
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is NoSuchElementException)
    }

    @Test
    fun `placeBid propagates updated vehicle to getVehicles flow`() = runTest(testDispatcher) {
        val v1 = createTestDto("v-1", currentBid = 10000.0, bidCount = 1)
        val v2 = createTestDto("v-2", currentBid = 15000.0, bidCount = 2)
        val repository = VehicleRepositoryImpl(FakeDataSource(listOf(v1, v2)), testDispatcher)

        repository.getVehicles().test {
            val list1 = awaitItem()
            assertEquals(2, list1.size)
            assertEquals(10000.0, list1[0].currentBid)

            repository.placeBid("v-1", 12000.0)

            val list2 = awaitItem()
            assertEquals(12000.0, list2[0].currentBid)
            assertEquals(2, list2[0].bidCount)

            cancelAndIgnoreRemainingEvents()
        }
    }
}
