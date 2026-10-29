package com.anenha.block.domain.usecase

import com.anenha.block.domain.model.Vehicle
import com.anenha.block.domain.repository.VehicleRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchVehiclesUseCase @Inject constructor(
    private val repository: VehicleRepository
) {
    operator fun invoke(query: String): Flow<List<Vehicle>> = repository.searchVehicles(query)
}
