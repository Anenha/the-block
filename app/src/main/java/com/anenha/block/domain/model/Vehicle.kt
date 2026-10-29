package com.anenha.block.domain.model

data class Vehicle(
    val id: String,
    val vin: String,
    val year: Int,
    val make: String,
    val model: String,
    val trim: String?,
    val bodyStyle: String,
    val exteriorColor: String,
    val interiorColor: String,
    val engine: String,
    val transmission: String,
    val drivetrain: String,
    val odometerKm: Int,
    val fuelType: String,
    val conditionGrade: Double,
    val conditionReport: String,
    val damageNotes: List<String>,
    val titleStatus: String,
    val location: VehicleLocation,
    val auctionDetails: AuctionDetails,
    val images: List<String>,
    val sellingDealership: String,
    val lot: String,
    val currentBid: Double?,
    val bidCount: Int
) {
    val title: String
        get() = "$make $model" + if (!trim.isNullOrBlank()) " $trim" else ""
}

data class VehicleLocation(
    val city: String,
    val province: String
) {
    val formatted: String
        get() = "$city, $province"
}

data class AuctionDetails(
    val auctionStartIso: String,
    val startingBid: Double,
    val reservePrice: Double?,
    val buyNowPrice: Double?
) {
    val isReserveMet: (currentBid: Double) -> Boolean = { currentBid ->
        reservePrice == null || currentBid >= reservePrice
    }
}
