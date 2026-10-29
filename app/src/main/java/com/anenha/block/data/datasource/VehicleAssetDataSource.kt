package com.anenha.block.data.datasource

import android.content.Context
import com.anenha.block.data.dto.VehicleDto
import com.anenha.block.di.IoDispatcher
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.io.InputStreamReader
import javax.inject.Inject

interface VehicleAssetDataSource {
    suspend fun loadVehicles(): List<VehicleDto>
}

class VehicleAssetDataSourceImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val json: Json,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : VehicleAssetDataSource {

    override suspend fun loadVehicles(): List<VehicleDto> = withContext(ioDispatcher) {
        context.assets.open("vehicles.json").use { inputStream ->
            InputStreamReader(inputStream, Charsets.UTF_8).use { reader ->
                val jsonContent = reader.readText()
                json.decodeFromString<List<VehicleDto>>(jsonContent)
            }
        }
    }
}
