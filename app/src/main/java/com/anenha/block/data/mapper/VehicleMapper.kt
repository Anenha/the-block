package com.anenha.block.data.mapper

import com.anenha.block.data.dto.VehicleDto
import com.anenha.block.domain.model.AuctionDetails
import com.anenha.block.domain.model.Vehicle
import com.anenha.block.domain.model.VehicleLocation

fun VehicleDto.toDomain(): Vehicle = Vehicle(
    id = id,
    vin = vin,
    year = year,
    make = make,
    model = model,
    trim = trim,
    bodyStyle = bodyStyle,
    exteriorColor = exteriorColor,
    interiorColor = interiorColor,
    engine = engine,
    transmission = transmission,
    drivetrain = drivetrain,
    odometerKm = odometerKm,
    fuelType = fuelType,
    conditionGrade = conditionGrade,
    conditionReport = conditionReport,
    damageNotes = damageNotes,
    titleStatus = titleStatus,
    location = VehicleLocation(city = city, province = province),
    auctionDetails = AuctionDetails(
        auctionStartIso = auctionStart,
        startingBid = startingBid,
        reservePrice = reservePrice,
        buyNowPrice = buyNowPrice
    ),
    images = images,
    sellingDealership = sellingDealership,
    lot = lot,
    currentBid = currentBid,
    bidCount = bidCount
)
