package com.anenha.block.features.detail

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.SubcomposeAsyncImage
import com.anenha.block.R
import com.anenha.block.domain.model.AuctionDetails
import com.anenha.block.domain.model.Vehicle
import com.anenha.block.domain.model.VehicleLocation
import com.anenha.block.ui.components.BidBottomSheet
import com.anenha.block.ui.components.LotNumberBadge
import com.anenha.block.ui.components.VehicleBadgesRow
import com.anenha.block.ui.theme.BlockTheme
import com.anenha.block.ui.theme.FullShape
import com.anenha.block.ui.theme.extendedColors
import com.anenha.block.ui.theme.spacing
import com.anenha.block.ui.util.formatAuctionSchedule
import com.anenha.block.ui.util.formatCurrency
import com.anenha.block.ui.util.formatNumber
import com.anenha.block.ui.util.parseVehicleColor
import kotlin.math.roundToInt

/**
 * Stateful entry point for the Vehicle Details screen.
 * Collects state from [VehicleDetailViewModel] and manages navigation back clicks.
 */
@Composable
fun VehicleDetailRoute(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: VehicleDetailViewModel = hiltViewModel(),
    onPlaceBidClick: (Vehicle) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showBidSheet by remember { mutableStateOf(false) }

    VehicleDetailScreen(
        uiState = uiState,
        onBackClick = onBackClick,
        onRetry = { viewModel.loadVehicle() },
        onPlaceBidClick = { vehicle ->
            showBidSheet = true
            onPlaceBidClick(vehicle)
        },
        modifier = modifier
    )

    if (showBidSheet && uiState is VehicleDetailUiState.Success) {
        val currentVehicle = (uiState as VehicleDetailUiState.Success).vehicle
        BidBottomSheet(
            vehicle = currentVehicle,
            onDismissRequest = { showBidSheet = false },
            onPlaceBid = { _, amount ->
                viewModel.placeBid(amount)
                showBidSheet = false
            }
        )
    }
}

/**
 * Stateless Vehicle Details screen displaying comprehensive vehicle telemetry,
 * specifications, auction scheduling, and inspection reports matching design reference.
 */
@Composable
fun VehicleDetailScreen(
    uiState: VehicleDetailUiState,
    onBackClick: () -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
    onPlaceBidClick: (Vehicle) -> Unit = {}
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            DetailTopBar(onBackClick = onBackClick)
        },
        bottomBar = {
            if (uiState is VehicleDetailUiState.Success) {
                DetailBottomBar(
                    vehicle = uiState.vehicle,
                    onPlaceBidClick = { onPlaceBidClick(uiState.vehicle) }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (uiState) {
                is VehicleDetailUiState.Loading -> {
                    DetailLoadingContent(modifier = Modifier.align(Alignment.Center))
                }

                is VehicleDetailUiState.Success -> {
                    DetailSuccessContent(
                        vehicle = uiState.vehicle,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                is VehicleDetailUiState.NotFound -> {
                    DetailNotFoundContent(
                        onBackClick = onBackClick,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                is VehicleDetailUiState.Error -> {
                    DetailErrorContent(
                        message = uiState.message,
                        onRetry = onRetry,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}

/**
 * Top app bar with back navigation and title.
 */
@Composable
private fun DetailTopBar(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val spacing = MaterialTheme.spacing

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = spacing.marginMobile, vertical = spacing.spaceMd),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(spacing.spaceMd)
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .clickable(onClick = onBackClick),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = stringResource(R.string.cd_navigate_back),
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.size(24.dp)
            )
        }

        Text(
            text = stringResource(R.string.title_vehicle_details),
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

/**
 * Complete scrollable detail content for the vehicle.
 */
@Composable
private fun DetailSuccessContent(
    vehicle: Vehicle,
    modifier: Modifier = Modifier
) {
    val spacing = MaterialTheme.spacing

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(bottom = spacing.spaceXl)
    ) {
        // 1. Hero Image with Overlay Badges
        VehicleHeroSection(vehicle = vehicle)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = spacing.marginMobile, vertical = spacing.spaceMd),
            verticalArrangement = Arrangement.spacedBy(spacing.spaceLg)
        ) {
            // 2. Vehicle Title & Identification
            VehicleHeaderInfo(vehicle = vehicle)

            // 3. Auction Countdown & Bidding Telemetry Card
            AuctionTelemetryCard(vehicle = vehicle)

            // 4. Telemetry & Specifications 2x2 Grid + Colors
            TelemetrySpecificationsSection(vehicle = vehicle)

            // 5. Certified Inspection & Damage History
            CertifiedInspectionSection(vehicle = vehicle)

            // 6. Selling Dealership Card
            SellingDealerCard(vehicle = vehicle)
        }
    }
}

/**
 * Hero image gallery carousel using HorizontalPager with circle indicators and overlay tags.
 */
@Composable
private fun VehicleHeroSection(vehicle: Vehicle) {
    val extendedColors = MaterialTheme.extendedColors
    val spacing = MaterialTheme.spacing
    val images = vehicle.images
    val pageCount = images.size.coerceAtLeast(1)
    val pagerState = rememberPagerState(pageCount = { pageCount })

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(16f / 10f)
            .background(MaterialTheme.colorScheme.surfaceContainerLowest)
    ) {
        if (images.isNotEmpty()) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                val imageUrl = images[page]
                SubcomposeAsyncImage(
                    model = imageUrl,
                    contentDescription = stringResource(R.string.cd_vehicle_photo, vehicle.title, page + 1),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                    loading = {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                color = extendedColors.performanceCyan,
                                strokeWidth = 2.5.dp,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    },
                    error = {
                        HeroFallback(title = vehicle.title)
                    }
                )
            }
        } else {
            HeroFallback(title = vehicle.title)
        }

        // Top-Left Floating Badge: LOT #
        LotNumberBadge(
            lot = vehicle.lot,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(spacing.spaceMd)
        )

        // Bottom Overlays: Circle Indicators & Badges
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = spacing.spaceMd, vertical = spacing.spaceSm),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(spacing.spaceSm)
        ) {
            // Circle Indicators
            if (pageCount > 1) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(FullShape)
                        .background(Color.Black.copy(alpha = 0.40f))
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    repeat(pageCount) { iteration ->
                        val isSelected = pagerState.currentPage == iteration
                        Box(
                            modifier = Modifier
                                .size(if (isSelected) 8.dp else 6.dp)
                                .clip(CircleShape)
                                .background(
                                    if (isSelected) Color.White else Color.White.copy(alpha = 0.45f)
                                )
                        )
                    }
                }
            }
        }
    }
}

/**
 * Fallback when hero vehicle image is unavailable.
 */
@Composable
private fun HeroFallback(title: String) {
    val extendedColors = MaterialTheme.extendedColors
    val spacing = MaterialTheme.spacing

    Box(
        modifier = Modifier
            .fillMaxSize()
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
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    color = extendedColors.performanceCyan,
                    modifier = Modifier.padding(
                        horizontal = spacing.spaceLg,
                        vertical = spacing.spaceSm
                    )
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

/**
 * Vehicle title, condition tags, VIN, and location row.
 */
@Composable
private fun VehicleHeaderInfo(vehicle: Vehicle) {
    val extendedColors = MaterialTheme.extendedColors
    val spacing = MaterialTheme.spacing

    Column(verticalArrangement = Arrangement.spacedBy(spacing.spaceSm)) {
        // Badges Row
        VehicleBadgesRow(
            conditionGrade = vehicle.conditionGrade,
            titleStatus = vehicle.titleStatus
        )

        // Vehicle Main Title (Year Make Model Trim)
        Text(
            text = "${vehicle.year} ${vehicle.title}",
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
                letterSpacing = (-0.5).sp
            ),
            color = MaterialTheme.colorScheme.onBackground
        )

        // VIN row
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(spacing.spaceSm)
        ) {
            Text(
                text = stringResource(R.string.label_vin),
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                color = extendedColors.textMuted
            )
            Text(
                text = vehicle.vin,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 1.sp
                ),
                color = extendedColors.textSecondary
            )
        }

        // Location & Dealership Row with Map View link
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(spacing.spaceXs),
                modifier = Modifier.weight(1f)
            ) {
                Canvas(modifier = Modifier.size(14.dp)) {
                    drawCircle(
                        color = extendedColors.performanceCyan,
                        radius = size.width * 0.35f,
                        center = Offset(size.width * 0.5f, size.height * 0.35f)
                    )
                    val path = Path().apply {
                        moveTo(size.width * 0.25f, size.height * 0.4f)
                        lineTo(size.width * 0.5f, size.height * 0.95f)
                        lineTo(size.width * 0.75f, size.height * 0.4f)
                    }
                    drawPath(path = path, color = extendedColors.performanceCyan)
                }
                Text(
                    text = "${vehicle.location.city}, ${vehicle.location.province} • ${vehicle.sellingDealership}",
                    style = MaterialTheme.typography.bodySmall,
                    color = extendedColors.textSecondary
                )
            }
        }
    }
}

/**
 * Auction status countdown and live bidding telemetry card.
 */
@Composable
private fun AuctionTelemetryCard(vehicle: Vehicle) {
    val extendedColors = MaterialTheme.extendedColors
    val spacing = MaterialTheme.spacing
    val (scheduleDate, scheduleTime) = formatAuctionSchedule(vehicle.auctionDetails.auctionStartIso)
    val currentBid = vehicle.currentBid ?: vehicle.auctionDetails.startingBid
    val isReserveMet = vehicle.auctionDetails.isReserveMet(currentBid)

    Column(verticalArrangement = Arrangement.spacedBy(spacing.spaceMd)) {
        // Section Header: Title + isReserveMet Badge
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.title_auction_details),
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground
            )

            Surface(
                shape = RoundedCornerShape(4.dp),
                color = if (isReserveMet) Color(0xFFE6F7ED) else Color(0xFFFDE8E4)
            ) {
                Text(
                    text = if (isReserveMet) {
                        stringResource(R.string.reserve_met_caps)
                    } else {
                        stringResource(R.string.reserve_not_met_caps)
                    },
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp
                    ),
                    color = if (isReserveMet) Color(0xFF0F6838) else Color(0xFF9E2A1A),
                    modifier = Modifier.padding(
                        horizontal = spacing.spaceSm,
                        vertical = spacing.spaceXs
                    )
                )
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, extendedColors.borderSubtle, MaterialTheme.shapes.large),
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.cardColors(containerColor = extendedColors.cardBackground)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(spacing.spaceMd),
                verticalArrangement = Arrangement.spacedBy(spacing.spaceMd)
            ) {
                // Scheduled Start & Activity Sub-Card
                Surface(
                    shape = MaterialTheme.shapes.medium,
                    color = MaterialTheme.colorScheme.surfaceContainerLow,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = spacing.spaceMd, vertical = spacing.spaceSm),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(spacing.spaceXs)) {
                            Text(
                                text = stringResource(R.string.label_scheduled_start),
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                color = extendedColors.textMuted
                            )
                            Text(
                                text = scheduleDate,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Text(
                                text = scheduleTime,
                                style = MaterialTheme.typography.bodySmall,
                                color = extendedColors.textSecondary
                            )
                        }

                        Column(verticalArrangement = Arrangement.spacedBy(spacing.spaceXs)) {
                            Text(
                                text = stringResource(R.string.label_activity),
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                color = extendedColors.textMuted
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(spacing.spaceXs)
                            ) {
                                Text(
                                    text = vehicle.bidCount.toString(),
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = extendedColors.cyanDeep
                                )
                                Text(
                                    text = stringResource(R.string.label_pre_bids_placed),
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                                    color = extendedColors.textSecondary
                                )
                            }
                        }
                    }
                }

                // Pricing Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    // Current Highest Bid
                    Column(verticalArrangement = Arrangement.spacedBy(spacing.spaceXs)) {
                        Text(
                            text = stringResource(R.string.label_current_highest_bid),
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                            color = extendedColors.textMuted
                        )
                        Row(
                            verticalAlignment = Alignment.Bottom,
                            horizontalArrangement = Arrangement.spacedBy(spacing.spaceXs)
                        ) {
                            Text(
                                text = formatCurrency(currentBid),
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 32.sp
                                ),
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Text(
                                text = stringResource(R.string.currency_cad),
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = extendedColors.textMuted,
                                modifier = Modifier.padding(bottom = 6.dp)
                            )
                        }
                    }

                    // Starting Bid & Reserve Info
                    Column(
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.spacedBy(spacing.spaceXs)
                    ) {
                        Text(
                            text = stringResource(R.string.label_starting_bid),
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                            color = extendedColors.textMuted
                        )
                        Text(
                            text = formatCurrency(vehicle.auctionDetails.startingBid),
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = extendedColors.textSecondary
                        )
                        if (vehicle.auctionDetails.reservePrice != null) {
                            Text(
                                text = stringResource(
                                    R.string.reserve_amount,
                                    formatCurrency(vehicle.auctionDetails.reservePrice)
                                ),
                                style = MaterialTheme.typography.labelSmall,
                                color = extendedColors.textMuted
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * 2x2 Telemetry specifications grid (Odometer, Engine, Drivetrain, Fuel) and color chips.
 */
@Composable
private fun TelemetrySpecificationsSection(vehicle: Vehicle) {
    val extendedColors = MaterialTheme.extendedColors
    val spacing = MaterialTheme.spacing

    Column(verticalArrangement = Arrangement.spacedBy(spacing.spaceMd)) {
        // Section Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.title_telemetry_specs),
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        // 2x2 Grid of Spec Tiles
        Column(verticalArrangement = Arrangement.spacedBy(spacing.spaceSm)) {
            // Row 1: Odometer & Engine
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(spacing.spaceSm)
            ) {
                SpecTile(
                    label = stringResource(R.string.label_odometer),
                    value = formatNumber(vehicle.odometerKm),
                    unit = stringResource(R.string.unit_km),
                    iconType = SpecIcon.ODOMETER,
                    modifier = Modifier.weight(1f)
                )
                SpecTile(
                    label = stringResource(R.string.label_engine),
                    value = vehicle.engine,
                    unit = null,
                    iconType = SpecIcon.ENGINE,
                    modifier = Modifier.weight(1f)
                )
            }

            // Row 2: Drivetrain & Fuel Type
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(spacing.spaceSm)
            ) {
                SpecTile(
                    label = stringResource(R.string.label_drivetrain),
                    value = vehicle.drivetrain.uppercase(),
                    subtitle = vehicle.transmission.replaceFirstChar { it.uppercase() },
                    iconType = SpecIcon.DRIVETRAIN,
                    modifier = Modifier.weight(1f)
                )
                SpecTile(
                    label = stringResource(R.string.label_fuel_type),
                    value = vehicle.fuelType.replaceFirstChar { it.uppercase() },
                    subtitle = stringResource(R.string.fuel_octane_rec),
                    iconType = SpecIcon.FUEL,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Color Swatches Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(spacing.spaceSm)
        ) {
            ColorTile(
                label = stringResource(R.string.label_exterior),
                colorName = vehicle.exteriorColor,
                modifier = Modifier.weight(1f)
            )
            ColorTile(
                label = stringResource(R.string.label_interior),
                colorName = vehicle.interiorColor,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

private enum class SpecIcon { ODOMETER, ENGINE, DRIVETRAIN, FUEL }

@Composable
private fun SpecTile(
    label: String,
    value: String,
    unit: String? = null,
    subtitle: String? = null,
    iconType: SpecIcon,
    modifier: Modifier = Modifier
) {
    val extendedColors = MaterialTheme.extendedColors
    val spacing = MaterialTheme.spacing

    Card(
        modifier = modifier.border(1.dp, extendedColors.borderSubtle, MaterialTheme.shapes.medium),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = extendedColors.cardBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(spacing.spaceMd),
            verticalArrangement = Arrangement.spacedBy(spacing.spaceXs)
        ) {
            // Label Row + Vector Icon
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                    color = extendedColors.textMuted
                )
                SpecIconGraphic(iconType = iconType, color = extendedColors.cyanDeep)
            }

            // Value Row
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(spacing.spaceXs)
            ) {
                Text(
                    text = value,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground
                )
                if (!unit.isNullOrBlank()) {
                    Text(
                        text = unit,
                        style = MaterialTheme.typography.bodySmall,
                        color = extendedColors.textSecondary,
                        modifier = Modifier.padding(bottom = 2.dp)
                    )
                }
            }

            // Optional Subtitle
            if (!subtitle.isNullOrBlank()) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = extendedColors.textMuted
                )
            }
        }
    }
}

@Composable
private fun SpecIconGraphic(iconType: SpecIcon, color: Color) {
    Canvas(modifier = Modifier.size(16.dp)) {
        when (iconType) {
            SpecIcon.ODOMETER -> {
                // Gauge arc + needle
                drawArc(
                    color = color,
                    startAngle = 180f,
                    sweepAngle = 180f,
                    useCenter = false,
                    style = Stroke(2.dp.toPx())
                )
                drawLine(
                    color = color,
                    start = Offset(size.width * 0.5f, size.height * 0.7f),
                    end = Offset(size.width * 0.75f, size.height * 0.35f),
                    strokeWidth = 2.dp.toPx()
                )
            }

            SpecIcon.ENGINE -> {
                // Engine / Sliders graphic
                drawLine(
                    color = color,
                    start = Offset(size.width * 0.15f, size.height * 0.3f),
                    end = Offset(size.width * 0.85f, size.height * 0.3f),
                    strokeWidth = 1.5.dp.toPx()
                )
                drawLine(
                    color = color,
                    start = Offset(size.width * 0.15f, size.height * 0.7f),
                    end = Offset(size.width * 0.85f, size.height * 0.7f),
                    strokeWidth = 1.5.dp.toPx()
                )
                drawCircle(
                    color = color,
                    radius = 2.5.dp.toPx(),
                    center = Offset(size.width * 0.35f, size.height * 0.3f)
                )
                drawCircle(
                    color = color,
                    radius = 2.5.dp.toPx(),
                    center = Offset(size.width * 0.65f, size.height * 0.7f)
                )
            }

            SpecIcon.DRIVETRAIN -> {
                // Opposite arrows
                drawLine(
                    color = color,
                    start = Offset(size.width * 0.2f, size.height * 0.35f),
                    end = Offset(size.width * 0.8f, size.height * 0.35f),
                    strokeWidth = 1.5.dp.toPx()
                )
                drawLine(
                    color = color,
                    start = Offset(size.width * 0.65f, size.height * 0.2f),
                    end = Offset(size.width * 0.8f, size.height * 0.35f),
                    strokeWidth = 1.5.dp.toPx()
                )
                drawLine(
                    color = color,
                    start = Offset(size.width * 0.8f, size.height * 0.65f),
                    end = Offset(size.width * 0.2f, size.height * 0.65f),
                    strokeWidth = 1.5.dp.toPx()
                )
                drawLine(
                    color = color,
                    start = Offset(size.width * 0.35f, size.height * 0.8f),
                    end = Offset(size.width * 0.2f, size.height * 0.65f),
                    strokeWidth = 1.5.dp.toPx()
                )
            }

            SpecIcon.FUEL -> {
                // Fuel pump icon
                drawRect(
                    color = color,
                    topLeft = Offset(size.width * 0.2f, size.height * 0.2f),
                    size = androidx.compose.ui.geometry.Size(
                        size.width * 0.45f,
                        size.height * 0.7f
                    ),
                    style = Stroke(1.5.dp.toPx())
                )
                drawLine(
                    color = color,
                    start = Offset(size.width * 0.65f, size.height * 0.35f),
                    end = Offset(size.width * 0.85f, size.height * 0.35f),
                    strokeWidth = 1.5.dp.toPx()
                )
                drawLine(
                    color = color,
                    start = Offset(size.width * 0.85f, size.height * 0.35f),
                    end = Offset(size.width * 0.85f, size.height * 0.7f),
                    strokeWidth = 1.5.dp.toPx()
                )
            }
        }
    }
}

@Composable
private fun ColorTile(
    label: String,
    colorName: String,
    modifier: Modifier = Modifier
) {
    val extendedColors = MaterialTheme.extendedColors
    val spacing = MaterialTheme.spacing
    val swatchColor = parseVehicleColor(colorName)

    Card(
        modifier = modifier.border(1.dp, extendedColors.borderSubtle, MaterialTheme.shapes.medium),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = extendedColors.cardBackground)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(spacing.spaceMd),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(spacing.spaceSm)
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(swatchColor)
                    .border(1.dp, Color.Black.copy(alpha = 0.15f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.8f))
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                    color = extendedColors.textMuted
                )
                Text(
                    text = colorName,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}

/**
 * Certified inspection section with star rating, inspector quote, and damage history.
 */
@Composable
private fun CertifiedInspectionSection(vehicle: Vehicle) {
    val extendedColors = MaterialTheme.extendedColors
    val spacing = MaterialTheme.spacing

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, extendedColors.borderSubtle, MaterialTheme.shapes.large),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = extendedColors.cardBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(spacing.spaceMd),
            verticalArrangement = Arrangement.spacedBy(spacing.spaceMd)
        ) {
            // Header with Grade & Stars
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(
                        text = stringResource(R.string.label_certified_inspection),
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                        color = extendedColors.textMuted
                    )
                    Text(
                        text = stringResource(R.string.grade_format, vehicle.conditionGrade.toString()),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                // 5 Stars visual
                RatingStars(grade = vehicle.conditionGrade)
            }

            // Quoted Condition Report Card
            Surface(
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.surfaceContainerLow,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(spacing.spaceMd),
                    horizontalArrangement = Arrangement.spacedBy(spacing.spaceSm),
                    verticalAlignment = Alignment.Top
                ) {
                    Canvas(modifier = Modifier.size(18.dp)) {
                        // Shield with checkmark
                        val path = Path().apply {
                            moveTo(size.width * 0.5f, 0f)
                            lineTo(size.width, size.height * 0.25f)
                            lineTo(size.width * 0.5f, size.height)
                            lineTo(0f, size.height * 0.25f)
                            close()
                        }
                        drawPath(path = path, color = extendedColors.cyanDeep.copy(alpha = 0.15f))
                        drawPath(
                            path = path,
                            color = extendedColors.cyanDeep,
                            style = Stroke(1.5.dp.toPx())
                        )
                        val check = Path().apply {
                            moveTo(size.width * 0.35f, size.height * 0.5f)
                            lineTo(size.width * 0.48f, size.height * 0.65f)
                            lineTo(size.width * 0.7f, size.height * 0.35f)
                        }
                        drawPath(
                            path = check,
                            color = extendedColors.cyanDeep,
                            style = Stroke(1.5.dp.toPx(), cap = StrokeCap.Round)
                        )
                    }

                    Text(
                        text = "\"${vehicle.conditionReport}\"",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontStyle = FontStyle.Italic,
                            lineHeight = 20.sp
                        ),
                        color = extendedColors.textPrimary
                    )
                }
            }

            // Damage History Tile
            Surface(
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.surfaceContainerLow,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(spacing.spaceMd),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(spacing.spaceSm)
                ) {
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .clip(CircleShape)
                            .background(
                                if (vehicle.damageNotes.isEmpty()) extendedColors.telemetryEmerald.copy(
                                    alpha = 0.15f
                                )
                                else extendedColors.trackAmber.copy(alpha = 0.15f)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.size(12.dp)) {
                            if (vehicle.damageNotes.isEmpty()) {
                                val check = Path().apply {
                                    moveTo(size.width * 0.2f, size.height * 0.55f)
                                    lineTo(size.width * 0.45f, size.height * 0.8f)
                                    lineTo(size.width * 0.85f, size.height * 0.25f)
                                }
                                drawPath(
                                    path = check,
                                    color = extendedColors.telemetryEmerald,
                                    style = Stroke(2.dp.toPx(), cap = StrokeCap.Round)
                                )
                            } else {
                                drawLine(
                                    color = extendedColors.trackAmber,
                                    start = Offset(size.width * 0.5f, size.height * 0.2f),
                                    end = Offset(size.width * 0.5f, size.height * 0.65f),
                                    strokeWidth = 2.dp.toPx()
                                )
                                drawCircle(
                                    color = extendedColors.trackAmber,
                                    radius = 1.dp.toPx(),
                                    center = Offset(size.width * 0.5f, size.height * 0.85f)
                                )
                            }
                        }
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(
                            text = stringResource(R.string.title_damage_history),
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                            color = extendedColors.textMuted
                        )
                        Text(
                            text = if (vehicle.damageNotes.isEmpty()) {
                                stringResource(R.string.none_reported)
                            } else {
                                vehicle.damageNotes.joinToString(", ")
                            },
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RatingStars(grade: Double) {
    val filledStars = (grade.coerceIn(0.0, 5.0)).roundToInt()
    val activeColor = Color(0xFFF97316)
    val inactiveColor = Color(0xFFD8DAE1)

    Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
        for (i in 1..5) {
            Text(
                text = "★",
                fontSize = 18.sp,
                color = if (i <= filledStars) activeColor else inactiveColor
            )
        }
    }
}

/**
 * Selling dealer info card with location.
 */
@Composable
private fun SellingDealerCard(vehicle: Vehicle) {
    val extendedColors = MaterialTheme.extendedColors
    val spacing = MaterialTheme.spacing

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, extendedColors.borderSubtle, MaterialTheme.shapes.large),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = extendedColors.cardBackground)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(spacing.spaceMd),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(spacing.spaceMd)
        ) {
            // Storefront icon in rounded box
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = MaterialTheme.colorScheme.surfaceContainerLow,
                modifier = Modifier.size(44.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Canvas(modifier = Modifier.size(22.dp)) {
                        // Storefront icon
                        val stroke = 1.8.dp.toPx()
                        val roof = Path().apply {
                            moveTo(size.width * 0.1f, size.height * 0.35f)
                            lineTo(size.width * 0.5f, size.height * 0.1f)
                            lineTo(size.width * 0.9f, size.height * 0.35f)
                        }
                        drawPath(
                            path = roof,
                            color = extendedColors.cyanDeep,
                            style = Stroke(stroke)
                        )
                        drawRect(
                            color = extendedColors.cyanDeep,
                            topLeft = Offset(size.width * 0.2f, size.height * 0.35f),
                            size = androidx.compose.ui.geometry.Size(
                                size.width * 0.6f,
                                size.height * 0.55f
                            ),
                            style = Stroke(stroke)
                        )
                        drawRect(
                            color = extendedColors.cyanDeep,
                            topLeft = Offset(size.width * 0.4f, size.height * 0.55f),
                            size = androidx.compose.ui.geometry.Size(
                                size.width * 0.2f,
                                size.height * 0.35f
                            ),
                            style = Stroke(stroke)
                        )
                    }
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = stringResource(R.string.label_selling_dealer),
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                    color = extendedColors.textMuted
                )
                Text(
                    text = vehicle.sellingDealership,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(spacing.spaceXs)
                ) {
                    Text(
                        text = "📍",
                        fontSize = 11.sp
                    )
                    Text(
                        text = "${vehicle.location.city}, ${vehicle.location.province}",
                        style = MaterialTheme.typography.bodySmall,
                        color = extendedColors.textSecondary
                    )
                }
            }
        }
    }
}

/**
 * Sticky bottom action bar containing the primary "Place Bid" button.
 */
@Composable
private fun DetailBottomBar(
    vehicle: Vehicle,
    onPlaceBidClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val extendedColors = MaterialTheme.extendedColors
    val spacing = MaterialTheme.spacing
    val currentBid = vehicle.currentBid ?: vehicle.auctionDetails.startingBid
    val nextBid = currentBid + 500.0

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.background,
        shadowElevation = 8.dp,
        border = BorderStroke(1.dp, extendedColors.borderSubtle)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = spacing.marginMobile, vertical = spacing.spaceMd)
        ) {
            Button(
                onClick = onPlaceBidClick,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF006577), // Deep Teal from design
                    contentColor = Color.White
                ),
                contentPadding = PaddingValues(horizontal = spacing.spaceMd, vertical = 14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(spacing.spaceSm, Alignment.CenterHorizontally),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Canvas(modifier = Modifier.size(18.dp)) {
                        // Gavel / Mallet Bid icon
                        val stroke = 2.dp.toPx()
                        drawLine(
                            color = Color.White,
                            start = Offset(size.width * 0.2f, size.height * 0.8f),
                            end = Offset(size.width * 0.6f, size.height * 0.4f),
                            strokeWidth = stroke,
                            cap = StrokeCap.Round
                        )
                        drawLine(
                            color = Color.White,
                            start = Offset(size.width * 0.45f, size.height * 0.25f),
                            end = Offset(size.width * 0.75f, size.height * 0.55f),
                            strokeWidth = stroke * 1.8f,
                            cap = StrokeCap.Round
                        )
                        drawLine(
                            color = Color.White,
                            start = Offset(size.width * 0.1f, size.height * 0.9f),
                            end = Offset(size.width * 0.4f, size.height * 0.9f),
                            strokeWidth = stroke,
                            cap = StrokeCap.Round
                        )
                    }
                    Text(
                        text = stringResource(R.string.btn_place_bid),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                }
            }
        }
    }
}

/**
 * Loading state presentation.
 */
@Composable
private fun DetailLoadingContent(modifier: Modifier = Modifier) {
    val extendedColors = MaterialTheme.extendedColors
    val spacing = MaterialTheme.spacing

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(spacing.spaceMd)
    ) {
        CircularProgressIndicator(
            color = extendedColors.performanceCyan,
            strokeWidth = 3.dp,
            modifier = Modifier.size(44.dp)
        )
        Text(
            text = stringResource(R.string.loading_vehicle_telemetry),
            style = MaterialTheme.typography.bodyMedium,
            color = extendedColors.textSecondary
        )
    }
}

/**
 * Not found state presentation.
 */
@Composable
private fun DetailNotFoundContent(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val extendedColors = MaterialTheme.extendedColors
    val spacing = MaterialTheme.spacing

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = spacing.marginMobile)
            .border(1.dp, extendedColors.borderSubtle, MaterialTheme.shapes.medium),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = extendedColors.cardBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(spacing.spaceLg),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(spacing.spaceMd)
        ) {
            Text(
                text = stringResource(R.string.title_vehicle_not_found),
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                color = extendedColors.performanceCyan
            )
            Text(
                text = stringResource(R.string.msg_vehicle_not_found),
                style = MaterialTheme.typography.bodyMedium,
                color = extendedColors.textSecondary,
                textAlign = TextAlign.Center
            )
            Button(
                onClick = onBackClick,
                shape = MaterialTheme.shapes.small,
                colors = ButtonDefaults.buttonColors(
                    containerColor = extendedColors.cyanCore,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            ) {
                Text(
                    text = stringResource(R.string.btn_return_to_auctions),
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}

/**
 * Error state presentation.
 */
@Composable
private fun DetailErrorContent(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    val extendedColors = MaterialTheme.extendedColors
    val spacing = MaterialTheme.spacing

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = spacing.marginMobile)
            .border(1.dp, extendedColors.borderSubtle, MaterialTheme.shapes.medium),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = extendedColors.cardBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(spacing.spaceLg),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(spacing.spaceMd)
        ) {
            Text(
                text = stringResource(R.string.title_telemetry_error),
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                color = extendedColors.performanceCyan
            )
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = extendedColors.textSecondary,
                textAlign = TextAlign.Center
            )
            OutlinedButton(
                onClick = onRetry,
                shape = MaterialTheme.shapes.small,
                border = BorderStroke(1.dp, extendedColors.borderSubtle)
            ) {
                Text(
                    text = stringResource(R.string.btn_retry_telemetry),
                    style = MaterialTheme.typography.labelLarge,
                    color = extendedColors.textPrimary
                )
            }
        }
    }
}

// ==========================================
// Previews
// ==========================================

private val sampleMazda = Vehicle(
    id = "4e3cd74f-bb88-4efe-b234-bcb2f7474b40",
    vin = "CG2UAF4T8LRBBVWJY",
    year = 2025,
    make = "Mazda",
    model = "CX-5",
    trim = "Turbo",
    bodyStyle = "SUV",
    exteriorColor = "Deep Crystal Blue",
    interiorColor = "Light Grey",
    engine = "2.5L I4",
    transmission = "automatic",
    drivetrain = "FWD",
    odometerKm = 24534,
    fuelType = "gasoline",
    conditionGrade = 4.0,
    conditionReport = "Very clean vehicle inside and out. Minor cosmetic wear only. All electronics and features fully functional.",
    damageNotes = emptyList(),
    titleStatus = "clean",
    location = VehicleLocation("Mississauga", "ON"),
    auctionDetails = AuctionDetails(
        auctionStartIso = "2026-04-05T19:00:00",
        startingBid = 20500.0,
        reservePrice = 29000.0,
        buyNowPrice = null
    ),
    images = listOf(
        "https://placehold.co/800x600/1a1a2e/eaeaea?text=2025+Mazda+CX-5+Photo+1",
        "https://placehold.co/800x600/1a1a2e/eaeaea?text=2025+Mazda+CX-5+Photo+2"
    ),
    sellingDealership = "Highway 7 Auto Sales",
    lot = "A-0001",
    currentBid = 21500.0,
    bidCount = 11
)

@Preview(
    name = "Vehicle Details - Light Mode (As Designed)",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
private fun VehicleDetailLightPreview() {
    BlockTheme(darkTheme = false) {
        VehicleDetailScreen(
            uiState = VehicleDetailUiState.Success(vehicle = sampleMazda),
            onBackClick = {},
            onRetry = {}
        )
    }
}

@Preview(
    name = "Vehicle Details - Dark Mode",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun VehicleDetailDarkPreview() {
    BlockTheme(darkTheme = true) {
        VehicleDetailScreen(
            uiState = VehicleDetailUiState.Success(vehicle = sampleMazda),
            onBackClick = {},
            onRetry = {}
        )
    }
}
