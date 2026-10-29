package com.anenha.block.domain.usecase

import app.cash.turbine.test
import com.anenha.block.domain.model.AuctionDetails
import com.anenha.block.domain.model.Vehicle
import com.anenha.block.domain.model.VehicleLocation
import com.anenha.block.domain.repository.VehicleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class SearchVehiclesUseCaseTest {

    private fun createTestVehicle(id: String, make: String, model: String): Vehicle {
        return Vehicle(
            id = id,
            vin = "VIN-$id",
            year = 2024,
            make = make,
            model = model,
            trim = "Sport",
            bodyStyle = "Sedan",
            exteriorColor = "Red",
            interiorColor = "Black",
            engine = "2.0L Turbo",
            transmission = "Auto",
            drivetrain = "AWD",
            odometerKm = 10000,
            fuelType = "gasoline",
            conditionGrade = 4.2,
            conditionReport = "Good",
            damageNotes = emptyList(),
            titleStatus = "clean",
            location = VehicleLocation("Austin", "TX"),
            auctionDetails = AuctionDetails("2026-10-30T10:00:00Z", 25000.0, 30000.0, null),
            images = emptyList(),
            sellingDealership = "Metro Honda",
            lot = "H-01",
            currentBid = 27000.0,
            bidCount = 4
        )
    }

    @Test
    fun `invoke delegates search query to repository and emits matching vehicles`() = runTest {
        val civic = createTestVehicle("v-1", "Honda", "Civic")
        val fakeRepository = object : VehicleRepository {
            override fun getVehicles(): Flow<List<Vehicle>> = flowOf(emptyList())
            override fun getVehicleById(vehicleId: String): Flow<Vehicle?> = flowOf(null)
            override fun searchVehicles(query: String): Flow<List<Vehicle>> {
                return if (query == "civic") flowOf(listOf(civic)) else flowOf(emptyList())
            }
            override suspend fun placeBid(vehicleId: String, amount: Double): Result<Vehicle> =
                Result.failure(NotImplementedError())
        }

        val useCase = SearchVehiclesUseCase(fakeRepository)

        useCase("civic").test {
            val emission = awaitItem()
            assertEquals(1, emission.size)
            assertEquals("Civic", emission.first().model)
            awaitComplete()
        }
    }

    @Test
    fun `invoke emits empty list when query matches zero vehicles`() = runTest {
        val fakeRepository = object : VehicleRepository {
            override fun getVehicles(): Flow<List<Vehicle>> = flowOf(emptyList())
            override fun getVehicleById(vehicleId: String): Flow<Vehicle?> = flowOf(null)
            override fun searchVehicles(query: String): Flow<List<Vehicle>> = flowOf(emptyList())
            override suspend fun placeBid(vehicleId: String, amount: Double): Result<Vehicle> =
                Result.failure(NotImplementedError())
        }

        val useCase = SearchVehiclesUseCase(fakeRepository)

        useCase("nonexistent-brand").test {
            val emission = awaitItem()
            assertEquals(0, emission.size)
            awaitComplete()
        }
    }
}
