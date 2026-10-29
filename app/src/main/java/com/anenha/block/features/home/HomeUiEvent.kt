package com.anenha.block.features.home

import com.anenha.block.domain.model.Vehicle

/**
 * User-driven actions and intents dispatched from the Home screen.
 */
sealed interface HomeUiEvent {
    data class OnSearchQueryChange(val query: String) : HomeUiEvent
    data object OnClearSearch : HomeUiEvent
    data class OnSpecsClick(val vehicle: Vehicle) : HomeUiEvent
    data class OnBidClick(val vehicle: Vehicle) : HomeUiEvent
    data class OnPlaceBid(val vehicleId: String, val amount: Double) : HomeUiEvent
    data object OnRetry : HomeUiEvent
    data object OnRefresh : HomeUiEvent
}
