package com.anenha.block.features.home

import app.cash.turbine.test
import com.anenha.block.domain.model.AuctionDetails
import com.anenha.block.domain.model.Vehicle
import com.anenha.block.domain.model.VehicleLocation
import com.anenha.block.domain.repository.VehicleRepository
import com.anenha.block.domain.usecase.GetVehiclesUseCase
import com.anenha.block.domain.usecase.PlaceBidUseCase
import com.anenha.block.domain.usecase.SearchVehiclesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
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
class HomeViewModelTest {

    private val testDispatcher: TestDispatcher = StandardTestDispatcher()
    private lateinit var fakeRepository: FakeVehicleRepository
    private lateinit var getVehiclesUseCase: GetVehiclesUseCase
    private lateinit var searchVehiclesUseCase: SearchVehiclesUseCase
    private lateinit var placeBidUseCase: PlaceBidUseCase

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        fakeRepository = FakeVehicleRepository()
        getVehiclesUseCase = GetVehiclesUseCase(fakeRepository)
        searchVehiclesUseCase = SearchVehiclesUseCase(fakeRepository)
        placeBidUseCase = PlaceBidUseCase(fakeRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel(): HomeViewModel {
        return HomeViewModel(getVehiclesUseCase, searchVehiclesUseCase, placeBidUseCase)
    }

    @Test
    fun `initial state emits Success when repository emits non-empty vehicle list`() = runTest(testDispatcher) {
        val testVehicles = listOf(createSampleVehicle("v-1"), createSampleVehicle("v-2"))
        val vehiclesFlow = MutableSharedFlow<List<Vehicle>>(replay = 1)
        vehiclesFlow.emit(testVehicles)
        fakeRepository.vehiclesFlow = vehiclesFlow

        val viewModel = createViewModel()

        viewModel.uiState.test {
            // First item should be Loading
            assertEquals(HomeUiState.Loading, awaitItem())
            testScheduler.advanceUntilIdle()
            // Next item should be Success with vehicles
            val successState = awaitItem() as HomeUiState.Success
            assertEquals(2, successState.vehicles.size)
            assertEquals("v-1", successState.vehicles[0].id)
            assertEquals("v-2", successState.vehicles[1].id)
            assertEquals("", successState.searchQuery)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `initial state emits Empty when repository emits empty list`() = runTest(testDispatcher) {
        val vehiclesFlow = MutableSharedFlow<List<Vehicle>>(replay = 1)
        vehiclesFlow.emit(emptyList())
        fakeRepository.vehiclesFlow = vehiclesFlow

        val viewModel = createViewModel()

        viewModel.uiState.test {
            assertEquals(HomeUiState.Loading, awaitItem())
            testScheduler.advanceUntilIdle()
            assertEquals(HomeUiState.Empty, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `initial state emits Error when repository throws exception`() = runTest(testDispatcher) {
        fakeRepository.vehiclesFlow = flow {
            throw IllegalStateException("Network or disk read failed")
        }

        val viewModel = createViewModel()

        viewModel.uiState.test {
            assertEquals(HomeUiState.Loading, awaitItem())
            testScheduler.advanceUntilIdle()
            val errorState = awaitItem() as HomeUiState.Error
            assertTrue(errorState.message.contains("Network or disk read failed"))
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onRetry re-executes use case after error`() = runTest(testDispatcher) {
        // First emit an error
        fakeRepository.vehiclesFlow = flow {
            throw RuntimeException("Temporary failure")
        }

        val viewModel = createViewModel()

        viewModel.uiState.test {
            assertEquals(HomeUiState.Loading, awaitItem())
            testScheduler.advanceUntilIdle()
            assertTrue(awaitItem() is HomeUiState.Error)

            // Switch to successful flow and trigger retry
            val recoveredFlow = MutableSharedFlow<List<Vehicle>>(replay = 1)
            recoveredFlow.emit(listOf(createSampleVehicle("v-recovered")))
            fakeRepository.vehiclesFlow = recoveredFlow

            viewModel.onEvent(HomeUiEvent.OnRetry)
            testScheduler.advanceUntilIdle()

            val successState = awaitItem() as HomeUiState.Success
            assertEquals("v-recovered", successState.vehicles.first().id)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `typing fewer than 3 letters keeps all vehicles and updates searchQuery`() = runTest(testDispatcher) {
        val allVehicles = listOf(createSampleVehicle("v-1"), createSampleVehicle("v-2"))
        fakeRepository.vehiclesFlow = flowOf(allVehicles)

        val viewModel = createViewModel()
        testScheduler.advanceUntilIdle()

        viewModel.uiState.test {
            val initialState = awaitItem() as HomeUiState.Success
            assertEquals(2, initialState.vehicles.size)

            // Type 1 letter: "P"
            viewModel.onEvent(HomeUiEvent.OnSearchQueryChange("P"))
            testScheduler.advanceUntilIdle()
            val oneLetterState = awaitItem() as HomeUiState.Success
            assertEquals(2, oneLetterState.vehicles.size)
            assertEquals("P", oneLetterState.searchQuery)

            // Type 2 letters: "Po"
            viewModel.onEvent(HomeUiEvent.OnSearchQueryChange("Po"))
            testScheduler.advanceUntilIdle()
            val twoLetterState = awaitItem() as HomeUiState.Success
            assertEquals(2, twoLetterState.vehicles.size)
            assertEquals("Po", twoLetterState.searchQuery)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `typing 3 or more letters triggers searchVehiclesUseCase directly`() = runTest(testDispatcher) {
        val allVehicles = listOf(createSampleVehicle("v-1", make = "Porsche"), createSampleVehicle("v-2", make = "BMW"))
        val porscheVehicle = listOf(createSampleVehicle("v-1", make = "Porsche"))
        fakeRepository.vehiclesFlow = flowOf(allVehicles)
        fakeRepository.searchVehiclesHandler = { query ->
            if (query.lowercase().startsWith("por")) flowOf(porscheVehicle) else flowOf(emptyList())
        }

        val viewModel = createViewModel()
        testScheduler.advanceUntilIdle()

        viewModel.uiState.test {
            val initialState = awaitItem() as HomeUiState.Success
            assertEquals(2, initialState.vehicles.size)

            // Type 3 letters: "Por" -> search executed directly
            viewModel.onEvent(HomeUiEvent.OnSearchQueryChange("Por"))
            testScheduler.advanceUntilIdle()

            val searchResult = awaitItem() as HomeUiState.Success
            assertEquals(1, searchResult.vehicles.size)
            assertEquals("Porsche", searchResult.vehicles.first().make)
            assertEquals("Por", searchResult.searchQuery)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `deleting all characters or clicking clear restores full vehicle list`() = runTest(testDispatcher) {
        val allVehicles = listOf(createSampleVehicle("v-1", make = "Porsche"), createSampleVehicle("v-2", make = "BMW"))
        val porscheVehicle = listOf(createSampleVehicle("v-1", make = "Porsche"))
        fakeRepository.vehiclesFlow = flowOf(allVehicles)
        fakeRepository.searchVehiclesHandler = { query ->
            if (query == "Por") flowOf(porscheVehicle) else flowOf(emptyList())
        }

        val viewModel = createViewModel()
        testScheduler.advanceUntilIdle()

        viewModel.uiState.test {
            awaitItem() // Initial success with 2 vehicles

            // Perform search with 3 letters
            viewModel.onEvent(HomeUiEvent.OnSearchQueryChange("Por"))
            testScheduler.advanceUntilIdle()
            val filteredState = awaitItem() as HomeUiState.Success
            assertEquals(1, filteredState.vehicles.size)

            // Click Clear (✕)
            viewModel.onEvent(HomeUiEvent.OnClearSearch)
            testScheduler.advanceUntilIdle()

            val restoredState = awaitItem() as HomeUiState.Success
            assertEquals(2, restoredState.vehicles.size)
            assertEquals("", restoredState.searchQuery)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onEvent OnPlaceBid delegates to placeBidUseCase`() = runTest(testDispatcher) {
        var placedVehicleId = ""
        var placedAmount = 0.0
        fakeRepository.placeBidHandler = { id, amount ->
            placedVehicleId = id
            placedAmount = amount
            Result.success(createSampleVehicle(id, currentBid = amount))
        }

        val viewModel = createViewModel()
        viewModel.onEvent(HomeUiEvent.OnPlaceBid("v-100", 65000.0))
        testScheduler.advanceUntilIdle()

        assertEquals("v-100", placedVehicleId)
        assertEquals(65000.0, placedAmount, 0.0)
    }

    private fun createSampleVehicle(id: String, make: String = "Porsche", currentBid: Double? = 50000.0): Vehicle {
        return Vehicle(
            id = id,
            vin = "VIN-$id",
            year = 2024,
            make = make,
            model = "Taycan",
            trim = "Turbo GT",
            bodyStyle = "Sedan",
            exteriorColor = "Black",
            interiorColor = "Black",
            engine = "Electric",
            transmission = "Auto",
            drivetrain = "AWD",
            odometerKm = 1000,
            fuelType = "electric",
            conditionGrade = 5.0,
            conditionReport = "Mint",
            damageNotes = emptyList(),
            titleStatus = "clean",
            location = VehicleLocation("Austin", "TX"),
            auctionDetails = AuctionDetails("2026-10-27T12:00:00Z", 40000.0, 60000.0, null),
            images = listOf("https://example.com/car.jpg"),
            sellingDealership = "Apex Motors",
            lot = "001",
            currentBid = currentBid,
            bidCount = 1
        )
    }

    private class FakeVehicleRepository : VehicleRepository {
        var vehiclesFlow: Flow<List<Vehicle>> = emptyFlow()
        var searchVehiclesHandler: (String) -> Flow<List<Vehicle>> = { emptyFlow() }
        var placeBidHandler: (String, Double) -> Result<Vehicle> = { _, _ -> Result.failure(UnsupportedOperationException()) }

        override fun getVehicles(): Flow<List<Vehicle>> = vehiclesFlow

        override fun getVehicleById(vehicleId: String): Flow<Vehicle?> = emptyFlow()

        override fun searchVehicles(query: String): Flow<List<Vehicle>> = searchVehiclesHandler(query)

        override suspend fun placeBid(vehicleId: String, amount: Double): Result<Vehicle> {
            return placeBidHandler(vehicleId, amount)
        }
    }
}
