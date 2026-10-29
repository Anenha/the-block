package com.anenha.block.features.detail

import com.anenha.block.domain.model.Vehicle

/**
 * UI State for the Vehicle Details screen.
 */
sealed interface VehicleDetailUiState {
    data object Loading : VehicleDetailUiState

    data class Success(val vehicle: Vehicle) : VehicleDetailUiState

    data object NotFound : VehicleDetailUiState

    data class Error(val message: String) : VehicleDetailUiState
}
