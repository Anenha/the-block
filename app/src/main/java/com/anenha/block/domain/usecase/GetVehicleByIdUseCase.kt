package com.anenha.block.domain.usecase

import com.anenha.block.domain.model.Vehicle
import com.anenha.block.domain.repository.VehicleRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetVehicleByIdUseCase @Inject constructor(
    private val repository: VehicleRepository
) {
    operator fun invoke(vehicleId: String): Flow<Vehicle?> = repository.getVehicleById(vehicleId)
}
