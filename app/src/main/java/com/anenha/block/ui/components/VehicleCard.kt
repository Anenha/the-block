package com.anenha.block.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.SubcomposeAsyncImage
import com.anenha.block.R
import com.anenha.block.domain.model.AuctionDetails
import com.anenha.block.domain.model.Vehicle
import com.anenha.block.domain.model.VehicleLocation
import com.anenha.block.ui.theme.BlockTheme
import com.anenha.block.ui.theme.FullShape
import com.anenha.block.ui.theme.extendedColors
import com.anenha.block.ui.theme.metricVal
import com.anenha.block.ui.theme.spacing
import com.anenha.block.ui.util.formatCurrency
import com.anenha.block.ui.util.formatNumber

/**
 * Domain-aware VehicleCard composable.
 * Maps high-level [Vehicle] domain entity directly to the stateless UI card.
 */
@Composable
fun VehicleCard(
    vehicle: Vehicle,
    modifier: Modifier = Modifier,
    onSpecsClick: (Vehicle) -> Unit = {},
    onBidClick: (Vehicle) -> Unit = {},
) {
    VehicleCard(
        imageUrl = vehicle.images.firstOrNull(),
        year = vehicle.year,
        title = vehicle.title,
        odometerValue = formatNumber(vehicle.odometerKm),
        odometerUnit = stringResource(R.string.unit_km),
        location = vehicle.location.formatted,
        currentBid = vehicle.currentBid ?: vehicle.auctionDetails.startingBid,
        reservePrice = vehicle.auctionDetails.reservePrice,
        conditionGrade = vehicle.conditionGrade,
        titleStatus = vehicle.titleStatus,
        modifier = modifier,
        onSpecsClick = { onSpecsClick(vehicle) },
        onBidClick = { onBidClick(vehicle) },
    )
}

/**
 * Stateless VehicleCard composable.
 * Renders vehicle image, condition grade & title status badges, telemetry readout, current bid, and primary actions.
 * Strictly utilizes tokens from the app's defined MaterialTheme (colors, typography, shapes, spacing).
 */
@Composable
fun VehicleCard(
    imageUrl: String?,
    year: Int,
    title: String,
    odometerValue: String,
    location: String,
    currentBid: Double,
    reservePrice: Double?,
    modifier: Modifier = Modifier,
    odometerUnit: String,
    conditionGrade: Double? = null,
    titleStatus: String? = null,
    onSpecsClick: () -> Unit = {},
    onBidClick: () -> Unit = {},
) {
    val extendedColors = MaterialTheme.extendedColors
    val spacing = MaterialTheme.spacing

    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = extendedColors.borderSubtle,
                shape = MaterialTheme.shapes.large
            ),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = extendedColors.cardBackground
        )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Vehicle Hero Image & Badges Anchor
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
                    .clip(
                        MaterialTheme.shapes.large.copy(
                            bottomStart = androidx.compose.foundation.shape.CornerSize(0.dp),
                            bottomEnd = androidx.compose.foundation.shape.CornerSize(0.dp)
                        )
                    )
                    .background(MaterialTheme.colorScheme.surfaceContainerLowest)
            ) {
                if (!imageUrl.isNullOrBlank()) {
                    SubcomposeAsyncImage(
                        model = imageUrl,
                        contentDescription = title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize(),
                        loading = {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    color = extendedColors.performanceCyan,
                                    strokeWidth = 2.dp,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        },
                        error = {
                            VehicleImageFallback(
                                title = title,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    )
                } else {
                    VehicleImageFallback(
                        title = title,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                // Inset Badges (Top Start)
                VehicleBadgesRow(
                    conditionGrade = conditionGrade,
                    titleStatus = titleStatus,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(horizontal = spacing.spaceMd, vertical = spacing.spaceSm)
                )
            }

            // Card Body Content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(spacing.spaceMd),
                verticalArrangement = Arrangement.spacedBy(spacing.spaceMd)
            ) {
                // Year & Vehicle Title
                Column(
                    verticalArrangement = Arrangement.spacedBy(spacing.spaceXs)
                ) {
                    Text(
                        text = year.toString(),
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = extendedColors.performanceCyan
                    )
                    Text(
                        text = title,
                        style = MaterialTheme.typography.headlineMedium,
                        color = extendedColors.textPrimary
                    )
                }

                // Cockpit Telemetry Container Box
                Surface(
                    shape = MaterialTheme.shapes.medium,
                    color = MaterialTheme.colorScheme.surfaceContainerLowest,
                    border = BorderStroke(1.dp, extendedColors.borderSubtle),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = spacing.spaceMd, vertical = spacing.spaceSm),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Odometer Column
                        Column(
                            verticalArrangement = Arrangement.spacedBy(spacing.spaceXs),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = stringResource(R.string.label_odometer),
                                style = MaterialTheme.typography.labelSmall,
                                color = extendedColors.textMuted
                            )
                            Row(
                                verticalAlignment = Alignment.Bottom,
                                horizontalArrangement = Arrangement.spacedBy(spacing.spaceXs)
                            ) {
                                Text(
                                    text = odometerValue,
                                    style = MaterialTheme.typography.metricVal,
                                    color = extendedColors.performanceCyan
                                )
                                Text(
                                    text = odometerUnit,
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                                    color = extendedColors.performanceCyan,
                                    modifier = Modifier.padding(bottom = spacing.spaceXs)
                                )
                            }
                        }

                        // Location Column
                        Column(
                            verticalArrangement = Arrangement.spacedBy(spacing.spaceXs),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = stringResource(R.string.label_location),
                                style = MaterialTheme.typography.labelSmall,
                                color = extendedColors.textMuted
                            )
                            Text(
                                text = location,
                                style = MaterialTheme.typography.headlineSmall,
                                color = extendedColors.textPrimary
                            )
                        }
                    }
                }

                // Pricing & Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Bid & Reserve Cluster
                    Column(
                        verticalArrangement = Arrangement.spacedBy(spacing.spaceXs)
                    ) {
                        Text(
                            text = stringResource(R.string.label_bid),
                            style = MaterialTheme.typography.labelSmall,
                            color = extendedColors.textMuted
                        )
                        Text(
                            text = formatCurrency(currentBid),
                            style = MaterialTheme.typography.headlineMedium,
                            color = extendedColors.textPrimary
                        )

                        if (reservePrice != null) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(spacing.spaceXs)
                            ) {
                                Text(
                                    text = stringResource(R.string.reserve_amount, formatCurrency(reservePrice)),
                                    style = MaterialTheme.typography.labelMedium,
                                    color = extendedColors.telemetryEmerald
                                )
                            }
                        }
                    }

                    // Action Buttons (Specs & Bid Now)
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(spacing.spaceSm),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = onSpecsClick,
                            shape = MaterialTheme.shapes.small,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = extendedColors.glassBackground,
                                contentColor = extendedColors.textPrimary
                            ),
                            border = BorderStroke(1.dp, extendedColors.borderSubtle),
                            contentPadding = PaddingValues(
                                horizontal = spacing.spaceMd,
                                vertical = spacing.spaceSm
                            )
                        ) {
                            Text(
                                text = stringResource(R.string.btn_specs),
                                style = MaterialTheme.typography.labelLarge
                            )
                        }

                        Button(
                            onClick = onBidClick,
                            shape = MaterialTheme.shapes.small,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = extendedColors.performanceCyan,
                                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                            ),
                            contentPadding = PaddingValues(
                                horizontal = spacing.spaceMd,
                                vertical = spacing.spaceSm
                            ),
                            modifier = Modifier.shadow(
                                elevation = spacing.spaceSm,
                                shape = MaterialTheme.shapes.small,
                                ambientColor = extendedColors.performanceCyan,
                                spotColor = extendedColors.performanceCyan
                            )
                        ) {
                            Text(
                                text = stringResource(R.string.btn_bid_now),
                                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }
                }
            }
        }
    }
}


@Preview(name = "VehicleCard - Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun VehicleCardDarkPreview() {
    BlockTheme(darkTheme = true) {
        Surface(
            modifier = Modifier.padding(16.dp),
            color = MaterialTheme.colorScheme.background
        ) {
            VehicleCard(
                imageUrl = null,
                year = 2024,
                title = "Taycan Turbo GT Weissach",
                odometerValue = "17,420",
                odometerUnit = "km",
                location = "Austin, TX",
                currentBid = 185000.0,
                reservePrice = 200000.0,
                conditionGrade = 5.0,
                titleStatus = "clean"
            )
        }
    }
}

@Preview(name = "VehicleCard - Light Mode", showBackground = true)
@Composable
private fun VehicleCardLightPreview() {
    BlockTheme(darkTheme = false) {
        Surface(
            modifier = Modifier.padding(16.dp),
            color = MaterialTheme.colorScheme.background
        ) {
            VehicleCard(
                imageUrl = null,
                year = 2024,
                title = "Taycan Turbo GT Weissach",
                odometerValue = "1,420",
                odometerUnit = "km",
                location = "Austin, TX",
                currentBid = 185000.0,
                reservePrice = 200000.0,
                conditionGrade = 5.0,
                titleStatus = "clean"
            )
        }
    }
}

@Preview(name = "VehicleCard - Domain Model", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun VehicleCardDomainPreview() {
    val sampleVehicle = Vehicle(
        id = "sample-1",
        vin = "WP0AC2Y18RSA0001",
        year = 2024,
        make = "Porsche",
        model = "Taycan",
        trim = "Turbo GT Weissach",
        bodyStyle = "Sedan",
        exteriorColor = "Metallic Black",
        interiorColor = "Carbon Black",
        engine = "Dual Electric Motors",
        transmission = "Automatic 2-Speed",
        drivetrain = "AWD",
        odometerKm = 2285,
        fuelType = "electric",
        conditionGrade = 5.0,
        conditionReport = "Mint collector condition.",
        damageNotes = emptyList(),
        titleStatus = "clean",
        location = VehicleLocation(city = "Austin", province = "TX"),
        auctionDetails = AuctionDetails(
            auctionStartIso = "2026-10-27T12:00:00",
            startingBid = 150000.0,
            reservePrice = 200000.0,
            buyNowPrice = null
        ),
        images = listOf("https://images.unsplash.com/photo-1614162692292-7ac56d7f7f1e"),
        sellingDealership = "Austin Exotics",
        lot = "LOT-102",
        currentBid = 185000.0,
        bidCount = 14
    )

    BlockTheme(darkTheme = true) {
        Surface(
            modifier = Modifier.padding(16.dp),
            color = MaterialTheme.colorScheme.background
        ) {
            VehicleCard(vehicle = sampleVehicle)
        }
    }
}

/**
 * Fallback placeholder displayed when vehicle image is unavailable or loading fails.
 */
@Composable
private fun VehicleImageFallback(
    title: String,
    modifier: Modifier = Modifier
) {
    val extendedColors = MaterialTheme.extendedColors
    val spacing = MaterialTheme.spacing

    Box(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surfaceContainerLowest),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(spacing.spaceXs)
        ) {
            Surface(
                shape = FullShape,
                color = extendedColors.glassBackground,
                border = BorderStroke(1.dp, extendedColors.borderSubtle)
            ) {
                Text(
                    text = title.take(2).uppercase(),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = extendedColors.performanceCyan,
                    modifier = Modifier.padding(horizontal = spacing.spaceMd, vertical = spacing.spaceSm)
                )
            }
            Text(
                text = stringResource(R.string.auction_photo_pending),
                style = MaterialTheme.typography.labelSmall,
                color = extendedColors.textMuted
            )
        }
    }
}

