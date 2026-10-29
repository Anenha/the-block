package com.anenha.block.domain.repository

import com.anenha.block.domain.model.Vehicle
import kotlinx.coroutines.flow.Flow

interface VehicleRepository {
    /**
     * Emits the complete list of vehicles.
     * Subsequent emissions occur when vehicle state updates (e.g., when bids are placed).
     */
    fun getVehicles(): Flow<List<Vehicle>>

    /**
     * Emits the vehicle with matching [vehicleId], or null if not found.
     * Updates reactively when the vehicle state changes.
     */
    fun getVehicleById(vehicleId: String): Flow<Vehicle?>

    /**
     * Searches vehicles matching the given [query] across make, model, year, trim, vin, and dealership.
     */
    fun searchVehicles(query: String): Flow<List<Vehicle>>

    /**
     * Places a bid on a vehicle, validating that the amount exceeds the current bid.
     * Updates the in-memory vehicle state and increments the bid count.
     */
    suspend fun placeBid(vehicleId: String, amount: Double): Result<Vehicle>
}
