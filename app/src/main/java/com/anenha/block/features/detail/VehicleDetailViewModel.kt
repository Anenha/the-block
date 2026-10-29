package com.anenha.block.features.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anenha.block.domain.usecase.GetVehicleByIdUseCase
import com.anenha.block.domain.usecase.PlaceBidUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel managing the state for the Vehicle Details screen.
 * Extracts [vehicleId] from [SavedStateHandle] and collects [GetVehicleByIdUseCase].
 */
@HiltViewModel
class VehicleDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getVehicleByIdUseCase: GetVehicleByIdUseCase,
    private val placeBidUseCase: PlaceBidUseCase
) : ViewModel() {

    val vehicleId: String = savedStateHandle.get<String>("vehicleId").orEmpty()

    private val _uiState = MutableStateFlow<VehicleDetailUiState>(VehicleDetailUiState.Loading)
    val uiState: StateFlow<VehicleDetailUiState> = _uiState.asStateFlow()

    private var loadJob: Job? = null

    init {
        loadVehicle()
    }

    fun loadVehicle() {
        if (vehicleId.isBlank()) {
            _uiState.value = VehicleDetailUiState.NotFound
            return
        }

        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            _uiState.value = VehicleDetailUiState.Loading
            getVehicleByIdUseCase(vehicleId)
                .catch { exception ->
                    _uiState.value = VehicleDetailUiState.Error(
                        message = exception.localizedMessage ?: "Failed to load vehicle telemetry"
                    )
                }
                .collect { vehicle ->
                    _uiState.value = if (vehicle != null) {
                        VehicleDetailUiState.Success(vehicle)
                    } else {
                        VehicleDetailUiState.NotFound
                    }
                }
        }
    }

    fun placeBid(amount: Double) {
        if (vehicleId.isBlank()) return
        viewModelScope.launch {
            placeBidUseCase(vehicleId, amount)
        }
    }
}
