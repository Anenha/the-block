package com.anenha.block.features.detail

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.anenha.block.domain.model.AuctionDetails
import com.anenha.block.domain.model.Vehicle
import com.anenha.block.domain.model.VehicleLocation
import com.anenha.block.domain.repository.VehicleRepository
import com.anenha.block.domain.usecase.GetVehicleByIdUseCase
import com.anenha.block.domain.usecase.PlaceBidUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class VehicleDetailViewModelTest {

    private val testDispatcher: TestDispatcher = StandardTestDispatcher()
    private lateinit var fakeRepository: FakeVehicleRepository
    private lateinit var getVehicleByIdUseCase: GetVehicleByIdUseCase
    private lateinit var placeBidUseCase: PlaceBidUseCase

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        fakeRepository = FakeVehicleRepository()
        getVehicleByIdUseCase = GetVehicleByIdUseCase(fakeRepository)
        placeBidUseCase = PlaceBidUseCase(fakeRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel(savedStateHandle: SavedStateHandle): VehicleDetailViewModel {
        return VehicleDetailViewModel(savedStateHandle, getVehicleByIdUseCase, placeBidUseCase)
    }

    @Test
    fun `initial state emits Success with correct title when vehicle is found`() = runTest(testDispatcher) {
        val testVehicle = createSampleVehicle("v-101", make = "Porsche", model = "911 GT3 RS", trim = "Weissach")
        val flow = MutableSharedFlow<Vehicle?>(replay = 1)
        flow.emit(testVehicle)
        fakeRepository.vehicleByIdFlow = flow

        val savedStateHandle = SavedStateHandle(mapOf("vehicleId" to "v-101"))
        val viewModel = createViewModel(savedStateHandle)

        viewModel.uiState.test {
            assertEquals(VehicleDetailUiState.Loading, awaitItem())
            testScheduler.advanceUntilIdle()

            val successState = awaitItem() as VehicleDetailUiState.Success
            assertEquals("v-101", successState.vehicle.id)
            assertEquals("Porsche 911 GT3 RS Weissach", successState.vehicle.title)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `initial state emits NotFound when vehicle ID does not exist`() = runTest(testDispatcher) {
        val flow = MutableSharedFlow<Vehicle?>(replay = 1)
        flow.emit(null)
        fakeRepository.vehicleByIdFlow = flow

        val savedStateHandle = SavedStateHandle(mapOf("vehicleId" to "non-existent-id"))
        val viewModel = createViewModel(savedStateHandle)

        viewModel.uiState.test {
            assertEquals(VehicleDetailUiState.Loading, awaitItem())
            testScheduler.advanceUntilIdle()
            assertEquals(VehicleDetailUiState.NotFound, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `initial state emits NotFound when savedStateHandle has blank vehicleId`() = runTest(testDispatcher) {
        val savedStateHandle = SavedStateHandle()
        val viewModel = createViewModel(savedStateHandle)

        viewModel.uiState.test {
            assertEquals(VehicleDetailUiState.NotFound, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `initial state emits Error when use case flow throws exception`() = runTest(testDispatcher) {
        fakeRepository.vehicleByIdFlow = flow {
            throw IllegalStateException("Telemetry server offline")
        }

        val savedStateHandle = SavedStateHandle(mapOf("vehicleId" to "v-error"))
        val viewModel = createViewModel(savedStateHandle)

        viewModel.uiState.test {
            assertEquals(VehicleDetailUiState.Loading, awaitItem())
            testScheduler.advanceUntilIdle()

            val errorState = awaitItem() as VehicleDetailUiState.Error
            assertTrue(errorState.message.contains("Telemetry server offline"))
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `loadVehicle retries and recovers after initial failure`() = runTest(testDispatcher) {
        fakeRepository.vehicleByIdFlow = flow {
            throw RuntimeException("Temporary failure")
        }

        val savedStateHandle = SavedStateHandle(mapOf("vehicleId" to "v-retry"))
        val viewModel = createViewModel(savedStateHandle)

        viewModel.uiState.test {
            assertEquals(VehicleDetailUiState.Loading, awaitItem())
            testScheduler.advanceUntilIdle()
            assertTrue(awaitItem() is VehicleDetailUiState.Error)

            // Recover repository flow and trigger retry
            val recoveredFlow = MutableSharedFlow<Vehicle?>(replay = 1)
            recoveredFlow.emit(createSampleVehicle("v-retry", make = "BMW", model = "M3 CS", trim = null))
            fakeRepository.vehicleByIdFlow = recoveredFlow

            viewModel.loadVehicle()
            assertEquals(VehicleDetailUiState.Loading, awaitItem())
            testScheduler.advanceUntilIdle()

            val successState = awaitItem() as VehicleDetailUiState.Success
            assertEquals("BMW M3 CS", successState.vehicle.title)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `placeBid delegates to placeBidUseCase with current vehicleId and amount`() = runTest(testDispatcher) {
        var placedVehicleId = ""
        var placedAmount = 0.0
        fakeRepository.placeBidHandler = { id, amount ->
            placedVehicleId = id
            placedAmount = amount
            Result.success(createSampleVehicle(id, currentBid = amount))
        }

        val savedStateHandle = SavedStateHandle(mapOf("vehicleId" to "v-101"))
        val viewModel = createViewModel(savedStateHandle)

        viewModel.placeBid(140000.0)
        testScheduler.advanceUntilIdle()

        assertEquals("v-101", placedVehicleId)
        assertEquals(140000.0, placedAmount, 0.0)
    }

    private fun createSampleVehicle(
        id: String,
        make: String = "TestMake",
        model: String = "TestModel",
        trim: String? = null,
        currentBid: Double = 135000.0
    ): Vehicle {
        return Vehicle(
            id = id,
            vin = "VIN-$id",
            year = 2025,
            make = make,
            model = model,
            trim = trim,
            bodyStyle = "Coupe",
            exteriorColor = "Apex Silver",
            interiorColor = "Black",
            engine = "Twin-Turbo 4.0L V8",
            transmission = "8-Speed Dual-Clutch",
            drivetrain = "RWD",
            odometerKm = 1200,
            fuelType = "gasoline",
            conditionGrade = 5.0,
            conditionReport = "Flawless",
            damageNotes = emptyList(),
            titleStatus = "clean",
            location = VehicleLocation("Toronto", "ON"),
            auctionDetails = AuctionDetails("2026-10-27T12:00:00Z", 120000.0, 150000.0, null),
            images = emptyList(),
            sellingDealership = "Performance Motors",
            lot = "LOT-99",
            currentBid = currentBid,
            bidCount = 8
        )
    }

    private class FakeVehicleRepository : VehicleRepository {
        var vehicleByIdFlow: Flow<Vehicle?> = emptyFlow()
        var placeBidHandler: (String, Double) -> Result<Vehicle> = { _, _ -> Result.failure(UnsupportedOperationException()) }

        override fun getVehicles(): Flow<List<Vehicle>> = emptyFlow()

        override fun getVehicleById(vehicleId: String): Flow<Vehicle?> = vehicleByIdFlow

        override fun searchVehicles(query: String): Flow<List<Vehicle>> = emptyFlow()

        override suspend fun placeBid(vehicleId: String, amount: Double): Result<Vehicle> {
            return placeBidHandler(vehicleId, amount)
        }
    }
}
