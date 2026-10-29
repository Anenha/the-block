package com.anenha.block.domain.usecase

import com.anenha.block.domain.model.Vehicle
import com.anenha.block.domain.repository.VehicleRepository
import javax.inject.Inject

class PlaceBidUseCase @Inject constructor(
    private val repository: VehicleRepository
) {
    suspend operator fun invoke(vehicleId: String, amount: Double): Result<Vehicle> {
        return repository.placeBid(vehicleId, amount)
    }
}
