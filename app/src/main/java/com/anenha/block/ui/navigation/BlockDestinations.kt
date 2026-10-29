package com.anenha.block.ui.navigation

/**
 * Top-level navigation destination contracts and route helpers for The Block.
 */
object BlockDestinations {
    const val HOME_ROUTE = "home"
    const val DETAIL_ROUTE = "detail/{vehicleId}"
    const val VEHICLE_ID_ARG = "vehicleId"

    fun detailRoute(vehicleId: String): String = "detail/$vehicleId"
}
