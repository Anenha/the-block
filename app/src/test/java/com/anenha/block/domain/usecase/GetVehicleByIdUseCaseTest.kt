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
import org.junit.Assert.assertNull
import org.junit.Test

class GetVehicleByIdUseCaseTest {

    private fun createTestVehicle(id: String): Vehicle {
        return Vehicle(
            id = id,
            vin = "VIN-$id",
            year = 2024,
            make = "Porsche",
            model = "Taycan",
            trim = "Turbo GT",
            bodyStyle = "Sedan",
            exteriorColor = "Cyan",
            interiorColor = "Black",
            engine = "Dual Electric",
            transmission = "Single-speed",
            drivetrain = "AWD",
            odometerKm = 2400,
            fuelType = "electric",
            conditionGrade = 4.9,
            conditionReport = "Pristine",
            damageNotes = emptyList(),
            titleStatus = "clean",
            location = VehicleLocation("Toronto", "Ontario"),
            auctionDetails = AuctionDetails("2026-10-30T10:00:00Z", 150000.0, 180000.0, null),
            images = emptyList(),
            sellingDealership = "Apex Auto",
            lot = "A-001",
            currentBid = 165000.0,
            bidCount = 8
        )
    }

    @Test
    fun `invoke delegates to repository and emits vehicle when found`() = runTest {
        val expectedVehicle = createTestVehicle("v-target")
        val fakeRepository = object : VehicleRepository {
            override fun getVehicles(): Flow<List<Vehicle>> = flowOf(emptyList())
            override fun getVehicleById(vehicleId: String): Flow<Vehicle?> {
                return if (vehicleId == "v-target") flowOf(expectedVehicle) else flowOf(null)
            }
            override fun searchVehicles(query: String): Flow<List<Vehicle>> = flowOf(emptyList())
            override suspend fun placeBid(vehicleId: String, amount: Double): Result<Vehicle> =
                Result.failure(NotImplementedError())
        }

        val useCase = GetVehicleByIdUseCase(fakeRepository)

        useCase("v-target").test {
            val emission = awaitItem()
            assertEquals(expectedVehicle, emission)
            awaitComplete()
        }
    }

    @Test
    fun `invoke emits null when vehicleId does not match any vehicle`() = runTest {
        val fakeRepository = object : VehicleRepository {
            override fun getVehicles(): Flow<List<Vehicle>> = flowOf(emptyList())
            override fun getVehicleById(vehicleId: String): Flow<Vehicle?> = flowOf(null)
            override fun searchVehicles(query: String): Flow<List<Vehicle>> = flowOf(emptyList())
            override suspend fun placeBid(vehicleId: String, amount: Double): Result<Vehicle> =
                Result.failure(NotImplementedError())
        }

        val useCase = GetVehicleByIdUseCase(fakeRepository)

        useCase("v-unknown").test {
            val emission = awaitItem()
            assertNull(emission)
            awaitComplete()
        }
    }
}
