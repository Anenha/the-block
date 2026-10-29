package com.anenha.block.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.anenha.block.features.detail.VehicleDetailRoute
import com.anenha.block.features.home.HomeRoute

/**
 * Top-level application NavHost controlling screen transitions.
 */
@Composable
fun BlockNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = BlockDestinations.HOME_ROUTE,
        modifier = modifier
    ) {
        composable(BlockDestinations.HOME_ROUTE) {
            HomeRoute(
                onSpecsClick = { vehicleId ->
                    navController.navigate(BlockDestinations.detailRoute(vehicleId))
                }
            )
        }

        composable(
            route = BlockDestinations.DETAIL_ROUTE,
            arguments = listOf(
                navArgument(BlockDestinations.VEHICLE_ID_ARG) {
                    type = NavType.StringType
                }
            )
        ) {
            VehicleDetailRoute(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}
