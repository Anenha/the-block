package com.anenha.block.domain.usecase

import com.anenha.block.domain.model.AuctionDetails
import com.anenha.block.domain.model.Vehicle
import com.anenha.block.domain.model.VehicleLocation
import com.anenha.block.domain.repository.VehicleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PlaceBidUseCaseTest {

    @Test
    fun `invoke delegates to repository and returns success with updated vehicle`() = runTest {
        val expectedVehicle = createTestVehicle("v-1", currentBid = 25000.0, bidCount = 2)
        val fakeRepository = object : VehicleRepository {
            override fun getVehicles(): Flow<List<Vehicle>> = emptyFlow()
            override fun getVehicleById(vehicleId: String): Flow<Vehicle?> = emptyFlow()
            override fun searchVehicles(query: String): Flow<List<Vehicle>> = emptyFlow()
            override suspend fun placeBid(vehicleId: String, amount: Double): Result<Vehicle> {
                assertEquals("v-1", vehicleId)
                assertEquals(25000.0, amount, 0.0)
                return Result.success(expectedVehicle)
            }
        }

        val useCase = PlaceBidUseCase(fakeRepository)
        val result = useCase("v-1", 25000.0)

        assertTrue(result.isSuccess)
        assertEquals(expectedVehicle, result.getOrNull())
    }

    @Test
    fun `invoke delegates to repository and returns failure on invalid bid`() = runTest {
        val fakeRepository = object : VehicleRepository {
            override fun getVehicles(): Flow<List<Vehicle>> = emptyFlow()
            override fun getVehicleById(vehicleId: String): Flow<Vehicle?> = emptyFlow()
            override fun searchVehicles(query: String): Flow<List<Vehicle>> = emptyFlow()
            override suspend fun placeBid(vehicleId: String, amount: Double): Result<Vehicle> {
                return Result.failure(IllegalArgumentException("Bid too low"))
            }
        }

        val useCase = PlaceBidUseCase(fakeRepository)
        val result = useCase("v-1", 1000.0)

        assertTrue(result.isFailure)
        assertEquals("Bid too low", result.exceptionOrNull()?.message)
    }

    private fun createTestVehicle(id: String, currentBid: Double, bidCount: Int): Vehicle {
        return Vehicle(
            id = id,
            vin = "VIN-$id",
            year = 2024,
            make = "Ford",
            model = "Bronco",
            trim = "Badlands",
            bodyStyle = "SUV",
            exteriorColor = "Area 51",
            interiorColor = "Black",
            engine = "2.7L V6",
            transmission = "Auto",
            drivetrain = "4WD",
            odometerKm = 15000,
            fuelType = "gasoline",
            conditionGrade = 4.5,
            conditionReport = "Clean",
            damageNotes = emptyList(),
            titleStatus = "clean",
            location = VehicleLocation("Denver", "CO"),
            auctionDetails = AuctionDetails("2026-10-27T12:00:00Z", 20000.0, 30000.0, null),
            images = emptyList(),
            sellingDealership = "Mile High Ford",
            lot = "A-12",
            currentBid = currentBid,
            bidCount = bidCount
        )
    }
}
