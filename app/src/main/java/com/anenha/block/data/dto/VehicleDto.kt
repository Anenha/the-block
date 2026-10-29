package com.anenha.block.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VehicleDto(
    @SerialName("id") val id: String,
    @SerialName("vin") val vin: String,
    @SerialName("year") val year: Int,
    @SerialName("make") val make: String,
    @SerialName("model") val model: String,
    @SerialName("trim") val trim: String? = null,
    @SerialName("body_style") val bodyStyle: String,
    @SerialName("exterior_color") val exteriorColor: String,
    @SerialName("interior_color") val interiorColor: String,
    @SerialName("engine") val engine: String,
    @SerialName("transmission") val transmission: String,
    @SerialName("drivetrain") val drivetrain: String,
    @SerialName("odometer_km") val odometerKm: Int,
    @SerialName("fuel_type") val fuelType: String,
    @SerialName("condition_grade") val conditionGrade: Double,
    @SerialName("condition_report") val conditionReport: String,
    @SerialName("damage_notes") val damageNotes: List<String> = emptyList(),
    @SerialName("title_status") val titleStatus: String,
    @SerialName("province") val province: String,
    @SerialName("city") val city: String,
    @SerialName("auction_start") val auctionStart: String,
    @SerialName("starting_bid") val startingBid: Double,
    @SerialName("reserve_price") val reservePrice: Double? = null,
    @SerialName("buy_now_price") val buyNowPrice: Double? = null,
    @SerialName("images") val images: List<String> = emptyList(),
    @SerialName("selling_dealership") val sellingDealership: String,
    @SerialName("lot") val lot: String,
    @SerialName("current_bid") val currentBid: Double?,
    @SerialName("bid_count") val bidCount: Int
)
