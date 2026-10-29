package com.anenha.block.data.repository

import com.anenha.block.data.datasource.VehicleAssetDataSource
import com.anenha.block.data.mapper.toDomain
import com.anenha.block.di.IoDispatcher
import com.anenha.block.domain.model.Vehicle
import com.anenha.block.domain.repository.VehicleRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VehicleRepositoryImpl @Inject constructor(
    private val assetDataSource: VehicleAssetDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : VehicleRepository {

    private val mutex = Mutex()
    // In-memory cache holding all vehicles indexed by vehicleId (LinkedHashMap preserves dataset order)
    private val _vehiclesState = MutableStateFlow<Map<String, Vehicle>?>(null)

    /**
     * Reads and parses vehicles.json strictly once.
     * Thread-safe and subsequent calls return the existing in-memory cache instantly.
     */
    private suspend fun ensureLoaded(): Map<String, Vehicle> {
        val current = _vehiclesState.value
        if (current != null) return current

        return mutex.withLock {
            val existing = _vehiclesState.value
            if (existing != null) {
                existing
            } else {
                val dtoList = assetDataSource.loadVehicles()
                val domainMap = LinkedHashMap<String, Vehicle>(dtoList.size)
                for (dto in dtoList) {
                    val domain = dto.toDomain()
                    domainMap[domain.id] = domain
                }
                _vehiclesState.value = domainMap
                domainMap
            }
        }
    }

    override fun getVehicles(): Flow<List<Vehicle>> = flow {
        ensureLoaded()
        emitAll(_vehiclesState.filterNotNull().map { it.values.toList() })
    }.flowOn(ioDispatcher)

    override fun getVehicleById(vehicleId: String): Flow<Vehicle?> = flow {
        ensureLoaded()
        emitAll(_vehiclesState.filterNotNull().map { it[vehicleId] })
    }.flowOn(ioDispatcher)

    override fun searchVehicles(query: String): Flow<List<Vehicle>> = flow {
        ensureLoaded()
        val normalizedQuery = query.trim().lowercase()
        emitAll(
            _vehiclesState
                .filterNotNull()
                .map { map ->
                    if (normalizedQuery.isBlank()) {
                        map.values.toList()
                    } else {
                        map.values.filter { vehicle ->
                            vehicle.make.lowercase().contains(normalizedQuery) ||
                            vehicle.model.lowercase().contains(normalizedQuery) ||
                            vehicle.year.toString().contains(normalizedQuery) ||
                            (vehicle.trim?.lowercase()?.contains(normalizedQuery) == true) ||
                            vehicle.vin.lowercase().contains(normalizedQuery) ||
                            vehicle.sellingDealership.lowercase().contains(normalizedQuery)
                        }
                    }
                }
        )
    }.flowOn(ioDispatcher)

    override suspend fun placeBid(vehicleId: String, amount: Double): Result<Vehicle> = withContext(ioDispatcher) {
        ensureLoaded()
        mutex.withLock {
            val currentMap = _vehiclesState.value?.toMutableMap()
                ?: return@withContext Result.failure(IllegalStateException("Vehicles data not initialized"))

            val vehicle = currentMap[vehicleId]
                ?: return@withContext Result.failure(NoSuchElementException("Vehicle not found: $vehicleId"))

            val currentBid = vehicle.currentBid ?: vehicle.auctionDetails.startingBid
            if (amount <= currentBid) {
                return@withContext Result.failure(
                    IllegalArgumentException("Bid amount ($$amount) must be higher than current bid ($$currentBid)")
                )
            }

            val updatedVehicle = vehicle.copy(
                currentBid = amount,
                bidCount = vehicle.bidCount + 1
            )
            currentMap[vehicleId] = updatedVehicle
            _vehiclesState.value = currentMap
            Result.success(updatedVehicle)
        }
    }
}
