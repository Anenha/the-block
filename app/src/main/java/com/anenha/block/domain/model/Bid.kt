package com.anenha.block.domain.model

data class Bid(
    val vehicleId: String,
    val amount: Double,
    val timestampMillis: Long = System.currentTimeMillis()
)
