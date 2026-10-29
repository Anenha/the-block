package com.anenha.block.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anenha.block.domain.usecase.GetVehiclesUseCase
import com.anenha.block.domain.usecase.PlaceBidUseCase
import com.anenha.block.domain.usecase.SearchVehiclesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel managing the state of the Home auction inventory feed and search filtering.
 * Injects [GetVehiclesUseCase], [SearchVehiclesUseCase], and [PlaceBidUseCase].
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getVehiclesUseCase: GetVehiclesUseCase,
    private val searchVehiclesUseCase: SearchVehiclesUseCase,
    private val placeBidUseCase: PlaceBidUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private var currentQuery: String = ""
    private var loadJob: Job? = null

    init {
        loadVehicles()
    }

    fun onEvent(event: HomeUiEvent) {
        when (event) {
            is HomeUiEvent.OnSearchQueryChange -> handleSearchQueryChange(event.query)
            is HomeUiEvent.OnClearSearch -> handleClearSearch()
            is HomeUiEvent.OnRetry -> executeFetch(currentQuery)
            is HomeUiEvent.OnRefresh -> refreshVehicles()
            is HomeUiEvent.OnSpecsClick -> {
                // Handled at navigation/host level
            }
            is HomeUiEvent.OnBidClick -> {
                // Handled at UI route level to open bottom sheet
            }
            is HomeUiEvent.OnPlaceBid -> placeBid(event.vehicleId, event.amount)
        }
    }

    fun placeBid(vehicleId: String, amount: Double) {
        viewModelScope.launch {
            placeBidUseCase(vehicleId, amount)
        }
    }

    private fun handleSearchQueryChange(query: String) {
        currentQuery = query
        if (query.length >= 3) {
            // Search directly when user types 3 or more letters
            executeFetch(query)
        } else {
            // When query has fewer than 3 letters or is deleted, remove filtered results and show all
            executeFetch(query = "")
        }
    }

    private fun handleClearSearch() {
        currentQuery = ""
        executeFetch(query = "")
    }

    private fun loadVehicles() {
        executeFetch(query = "")
    }

    private fun executeFetch(query: String) {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            val flow = if (query.length >= 3) {
                searchVehiclesUseCase(query)
            } else {
                getVehiclesUseCase()
            }

            flow
                .catch { exception ->
                    _uiState.value = HomeUiState.Error(
                        message = exception.localizedMessage ?: "Failed to load vehicles"
                    )
                }
                .collect { vehicles ->
                    _uiState.value = if (vehicles.isEmpty() && currentQuery.isEmpty()) {
                        HomeUiState.Empty
                    } else {
                        HomeUiState.Success(
                            vehicles = vehicles,
                            searchQuery = currentQuery
                        )
                    }
                }
        }
    }

    private fun refreshVehicles() {
        val currentState = _uiState.value
        if (currentState is HomeUiState.Success) {
            _uiState.value = currentState.copy(isRefreshing = true)
        }
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            val flow = if (currentQuery.length >= 3) {
                searchVehiclesUseCase(currentQuery)
            } else {
                getVehiclesUseCase()
            }

            flow
                .catch { exception ->
                    _uiState.value = HomeUiState.Error(
                        message = exception.localizedMessage ?: "Failed to refresh vehicles"
                    )
                }
                .collect { vehicles ->
                    _uiState.value = if (vehicles.isEmpty() && currentQuery.isEmpty()) {
                        HomeUiState.Empty
                    } else {
                        HomeUiState.Success(
                            vehicles = vehicles,
                            searchQuery = currentQuery,
                            isRefreshing = false
                        )
                    }
                }
        }
    }
}
