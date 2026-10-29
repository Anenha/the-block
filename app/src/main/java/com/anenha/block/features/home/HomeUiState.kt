package com.anenha.block.features.home

import com.anenha.block.domain.model.Vehicle

/**
 * Represents the distinct UI states for the Home vehicle auction feed.
 */
sealed interface HomeUiState {
    val searchQuery: String get() = ""

    data object Loading : HomeUiState

    data object Empty : HomeUiState

    data class Success(
        val vehicles: List<Vehicle>,
        override val searchQuery: String = "",
        val isRefreshing: Boolean = false
    ) : HomeUiState

    data class Error(
        val message: String
    ) : HomeUiState
}