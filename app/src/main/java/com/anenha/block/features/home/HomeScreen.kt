package com.anenha.block.features.home

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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.anenha.block.R
import com.anenha.block.domain.model.AuctionDetails
import com.anenha.block.domain.model.Vehicle
import com.anenha.block.domain.model.VehicleLocation
import com.anenha.block.ui.components.BidBottomSheet
import com.anenha.block.ui.components.LiveAuctionBadge
import com.anenha.block.ui.components.VehicleCard
import com.anenha.block.ui.theme.BlockTheme
import com.anenha.block.ui.theme.FullShape
import com.anenha.block.ui.theme.extendedColors
import com.anenha.block.ui.theme.spacing

/**
 * Stateful entry point for the Home vehicle auction feed screen.
 * Collects state from [HomeViewModel] and delegates user intents.
 */
@Composable
fun HomeRoute(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    onSpecsClick: (vehicleId: String) -> Unit = {},
    onBidClick: (Vehicle) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var biddingVehicle by remember { mutableStateOf<Vehicle?>(null) }

    HomeScreen(
        uiState = uiState,
        modifier = modifier,
        onEvent = { event ->
            when (event) {
                is HomeUiEvent.OnSpecsClick -> onSpecsClick(event.vehicle.id)
                is HomeUiEvent.OnBidClick -> {
                    biddingVehicle = event.vehicle
                    onBidClick(event.vehicle)
                }
                else -> viewModel.onEvent(event)
            }
        }
    )

    biddingVehicle?.let { vehicle ->
        val activeVehicle = (uiState as? HomeUiState.Success)
            ?.vehicles
            ?.find { it.id == vehicle.id } ?: vehicle

        BidBottomSheet(
            vehicle = activeVehicle,
            onDismissRequest = { biddingVehicle = null },
            onPlaceBid = { vehicleId, amount ->
                viewModel.onEvent(HomeUiEvent.OnPlaceBid(vehicleId, amount))
                biddingVehicle = null
            }
        )
    }
}

/**
 * Defines the responsive grid layout tiers for the auction inventory feed.
 *
 * - [Compact]: Single-column presentation optimized for handset portrait viewports.
 * - [Medium]: Two-column grid presentation for handset landscape and tablet portrait viewports.
 * - [Large]: Three-column grid presentation for large/tablet landscape viewports.
 */
enum class HomeFeedLayout(val columns: Int) {
    Compact(columns = 1),
    Medium(columns = 2),
    Large(columns = 3);

    companion object {
        /**
         * Resolves the feed layout tier based on the current Android [Configuration].
         */
        fun fromConfiguration(configuration: Configuration): HomeFeedLayout {
            val isTablet = configuration.smallestScreenWidthDp >= 600
            val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

            return when {
                isTablet && isLandscape -> Large
                isTablet || isLandscape -> Medium
                else -> Compact
            }
        }
    }
}

/**
 * Stateless Home screen rendering the auction inventory list and corresponding loading/error/empty states.
 * Adapts dynamically to [layout] tiers ([HomeFeedLayout.Compact], [HomeFeedLayout.Medium], [HomeFeedLayout.Large]).
 *
 * @param uiState Current high-level UI presentation state.
 * @param onEvent Lambda forwarding UI intents to the ViewModel or parent controller.
 * @param modifier Screen-level layout modifier.
 * @param layout Responsive grid layout configuration, defaulting to resolving from [LocalConfiguration].
 */
@Composable
fun HomeScreen(
    uiState: HomeUiState,
    onEvent: (HomeUiEvent) -> Unit,
    modifier: Modifier = Modifier,
    layout: HomeFeedLayout = HomeFeedLayout.fromConfiguration(LocalConfiguration.current)
) {
    val spacing = MaterialTheme.spacing
    val horizontalMargin = when (layout) {
        HomeFeedLayout.Compact -> spacing.marginMobile
        HomeFeedLayout.Medium, HomeFeedLayout.Large -> spacing.margin
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        when (uiState) {
            is HomeUiState.Loading -> {
                HomeLoadingContent(
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            is HomeUiState.Error -> {
                HomeErrorContent(
                    message = uiState.message,
                    onRetry = { onEvent(HomeUiEvent.OnRetry) },
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(horizontal = horizontalMargin)
                )
            }

            is HomeUiState.Empty -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            horizontal = horizontalMargin,
                            vertical = spacing.spaceMd
                        ),
                    verticalArrangement = Arrangement.spacedBy(spacing.spaceMd)
                ) {
                    HomeHeader(lotCount = 0)
                    HomeSearchBar(
                        query = uiState.searchQuery,
                        onQueryChange = { onEvent(HomeUiEvent.OnSearchQueryChange(it)) },
                        onClearClick = { onEvent(HomeUiEvent.OnClearSearch) }
                    )
                    HomeEmptyContent(
                        onRefresh = { onEvent(HomeUiEvent.OnRefresh) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    )
                }
            }

            is HomeUiState.Success -> {
                HomeSuccessContent(
                    vehicles = uiState.vehicles,
                    searchQuery = uiState.searchQuery,
                    onEvent = onEvent,
                    onSpecsClick = { onEvent(HomeUiEvent.OnSpecsClick(it)) },
                    onBidClick = { onEvent(HomeUiEvent.OnBidClick(it)) },
                    layout = layout
                )
            }
        }
    }
}

/**
 * Primary auction feed list displaying the brand header, search bar, and responsive vehicle cards grid.
 *
 * @param vehicles List of auction vehicles to display in the feed.
 * @param searchQuery Current search bar query string.
 * @param onEvent Lambda forwarding UI intents to the ViewModel.
 * @param onSpecsClick Callback triggered when user requests detailed vehicle specifications.
 * @param onBidClick Callback triggered when user taps the direct bid button on a vehicle card.
 * @param modifier Composable layout modifier.
 * @param layout Responsive grid layout configuration governing column count and spacing.
 */
@Composable
private fun HomeSuccessContent(
    vehicles: List<Vehicle>,
    searchQuery: String,
    onEvent: (HomeUiEvent) -> Unit,
    onSpecsClick: (Vehicle) -> Unit,
    onBidClick: (Vehicle) -> Unit,
    modifier: Modifier = Modifier,
    layout: HomeFeedLayout = HomeFeedLayout.fromConfiguration(LocalConfiguration.current)
) {
    val spacing = MaterialTheme.spacing
    val contentPadding = PaddingValues(
        horizontal = when (layout) {
            HomeFeedLayout.Compact -> spacing.marginMobile
            HomeFeedLayout.Medium, HomeFeedLayout.Large -> spacing.margin
        },
        vertical = spacing.spaceMd
    )
    val horizontalSpacing = when (layout) {
        HomeFeedLayout.Compact -> spacing.gutterMobile
        HomeFeedLayout.Medium, HomeFeedLayout.Large -> spacing.gutter
    }
    val verticalSpacing = when (layout) {
        HomeFeedLayout.Compact -> spacing.spaceMd
        HomeFeedLayout.Medium, HomeFeedLayout.Large -> spacing.spaceLg
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(layout.columns),
        modifier = modifier.fillMaxSize(),
        contentPadding = contentPadding,
        horizontalArrangement = Arrangement.spacedBy(horizontalSpacing),
        verticalArrangement = Arrangement.spacedBy(verticalSpacing)
    ) {
        item(key = "home_header", span = { GridItemSpan(maxLineSpan) }) {
            HomeHeader(lotCount = vehicles.size)
        }

        item(key = "home_search_bar", span = { GridItemSpan(maxLineSpan) }) {
            HomeSearchBar(
                query = searchQuery,
                onQueryChange = { onEvent(HomeUiEvent.OnSearchQueryChange(it)) },
                onClearClick = { onEvent(HomeUiEvent.OnClearSearch) }
            )
        }

        if (vehicles.isEmpty() && searchQuery.isNotBlank()) {
            item(key = "home_search_empty", span = { GridItemSpan(maxLineSpan) }) {
                HomeSearchEmptyContent(
                    query = searchQuery,
                    onClearClick = { onEvent(HomeUiEvent.OnClearSearch) }
                )
            }
        } else {
            items(
                items = vehicles,
                key = { it.id },
                contentType = { "vehicle_card" }
            ) { vehicle ->
                VehicleCard(
                    vehicle = vehicle,
                    modifier = Modifier.fillMaxWidth(),
                    onSpecsClick = onSpecsClick,
                    onBidClick = onBidClick
                )
            }
        }
    }
}

/**
 * Capsule Search Bar with cyan magnifying glass and clear button matching reference design.
 */
@Composable
fun HomeSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onClearClick: () -> Unit,
    modifier: Modifier = Modifier,
    placeholderText: String = stringResource(R.string.search_placeholder)
) {
    val extendedColors = MaterialTheme.extendedColors
    val spacing = MaterialTheme.spacing

    // Maintain local TextFieldValue to ensure 100% synchronous input and stable cursor positioning
    var textFieldValue by remember {
        mutableStateOf(
            TextFieldValue(
                text = query,
                selection = TextRange(query.length)
            )
        )
    }

    // Synchronize local state when query changes externally (e.g., cleared by parent)
    LaunchedEffect(query) {
        if (textFieldValue.text != query) {
            textFieldValue = TextFieldValue(
                text = query,
                selection = TextRange(query.length)
            )
        }
    }

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = FullShape,
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        border = BorderStroke(1.dp, extendedColors.borderSubtle)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = spacing.spaceMd, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(spacing.spaceSm)
        ) {
            // Cyan Magnifying Glass Icon
            Canvas(modifier = Modifier.size(20.dp)) {
                val cyanColor = extendedColors.performanceCyan
                val strokeWidth = 2.dp.toPx()
                val radius = size.width * 0.32f
                val center = Offset(size.width * 0.4f, size.height * 0.4f)
                drawCircle(
                    color = cyanColor,
                    radius = radius,
                    center = center,
                    style = Stroke(strokeWidth)
                )
                val handleStart = Offset(
                    center.x + radius * 0.7f,
                    center.y + radius * 0.7f
                )
                val handleEnd = Offset(size.width * 0.9f, size.height * 0.9f)
                drawLine(
                    color = cyanColor,
                    start = handleStart,
                    end = handleEnd,
                    strokeWidth = strokeWidth,
                    cap = StrokeCap.Round
                )
            }

            // Text Input Box
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.CenterStart
            ) {
                if (textFieldValue.text.isEmpty()) {
                    Text(
                        text = placeholderText,
                        style = MaterialTheme.typography.bodyLarge,
                        color = extendedColors.textMuted
                    )
                }
                BasicTextField(
                    value = textFieldValue,
                    onValueChange = { newValue ->
                        textFieldValue = newValue
                        if (newValue.text != query) {
                            onQueryChange(newValue.text)
                        }
                    },
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.onBackground
                    ),
                    cursorBrush = SolidColor(extendedColors.performanceCyan),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Trailing Clear Button (✕)
            if (textFieldValue.text.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .clickable {
                            textFieldValue = TextFieldValue("", TextRange.Zero)
                            onClearClick()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.size(12.dp)) {
                        val stroke = 2.dp.toPx()
                        val iconColor = extendedColors.textSecondary
                        drawLine(
                            color = iconColor,
                            start = Offset(0f, 0f),
                            end = Offset(size.width, size.height),
                            strokeWidth = stroke,
                            cap = StrokeCap.Round
                        )
                        drawLine(
                            color = iconColor,
                            start = Offset(size.width, 0f),
                            end = Offset(0f, size.height),
                            strokeWidth = stroke,
                            cap = StrokeCap.Round
                        )
                    }
                }
            }
        }
    }
}

/**
 * Brand header highlighting platform telemetry and active lot counts.
 */
@Composable
private fun HomeHeader(
    lotCount: Int,
    modifier: Modifier = Modifier
) {
    val extendedColors = MaterialTheme.extendedColors
    val spacing = MaterialTheme.spacing

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = spacing.spaceSm),
        verticalArrangement = Arrangement.spacedBy(spacing.spaceSm)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(spacing.spaceXs)) {
                Text(
                    text = stringResource(R.string.brand_name),
                    style = MaterialTheme.typography.labelSmall,
                    color = extendedColors.performanceCyan
                )
                Text(
                    text = stringResource(R.string.brand_tagline),
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            // Live Auction Telemetry Pill
            LiveAuctionBadge(lotCount = lotCount)
        }

        Text(
            text = stringResource(R.string.brand_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = extendedColors.textSecondary
        )
    }
}

/**
 * Empty search results presentation with a clear action.
 */
@Composable
private fun HomeSearchEmptyContent(
    query: String,
    onClearClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val extendedColors = MaterialTheme.extendedColors
    val spacing = MaterialTheme.spacing

    Card(
        modifier = modifier
            .fillMaxWidth()
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
                text = stringResource(R.string.title_no_vehicles_found),
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                color = extendedColors.performanceCyan
            )
            Text(
                text = stringResource(R.string.msg_no_vehicles_found, query),
                style = MaterialTheme.typography.bodyMedium,
                color = extendedColors.textSecondary,
                textAlign = TextAlign.Center
            )
            OutlinedButton(
                onClick = onClearClick,
                shape = MaterialTheme.shapes.small,
                border = BorderStroke(1.dp, extendedColors.borderSubtle)
            ) {
                Text(
                    text = stringResource(R.string.btn_clear_search),
                    style = MaterialTheme.typography.labelLarge,
                    color = extendedColors.textPrimary
                )
            }
        }
    }
}

/**
 * Loading state with performance cyan indicator and telemetry status.
 */
@Composable
private fun HomeLoadingContent(
    modifier: Modifier = Modifier
) {
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
            text = stringResource(R.string.loading_syncing_telemetry),
            style = MaterialTheme.typography.bodyMedium,
            color = extendedColors.textSecondary
        )
    }
}

/**
 * Error state presentation with retry intent action.
 */
@Composable
private fun HomeErrorContent(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    val extendedColors = MaterialTheme.extendedColors
    val spacing = MaterialTheme.spacing

    Card(
        modifier = modifier
            .fillMaxWidth()
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
            Button(
                onClick = onRetry,
                shape = MaterialTheme.shapes.small,
                colors = ButtonDefaults.buttonColors(
                    containerColor = extendedColors.cyanCore,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            ) {
                Text(
                    text = stringResource(R.string.btn_retry_connection),
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}

/**
 * Empty state presentation when no vehicles are available.
 */
@Composable
private fun HomeEmptyContent(
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    val extendedColors = MaterialTheme.extendedColors
    val spacing = MaterialTheme.spacing

    Card(
        modifier = modifier
            .fillMaxWidth()
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
                text = stringResource(R.string.title_no_lots_available),
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                color = extendedColors.performanceCyan
            )
            Text(
                text = stringResource(R.string.msg_no_lots_available),
                style = MaterialTheme.typography.bodyMedium,
                color = extendedColors.textSecondary,
                textAlign = TextAlign.Center
            )
            OutlinedButton(
                onClick = onRefresh,
                shape = MaterialTheme.shapes.small,
                border = BorderStroke(1.dp, extendedColors.borderSubtle)
            ) {
                Text(
                    text = stringResource(R.string.btn_refresh_feed),
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

private val sampleVehicle = Vehicle(
    id = "vehicle-sample-1",
    vin = "WP0AB2Y14PSA12345",
    year = 2024,
    make = "Porsche",
    model = "Taycan",
    trim = "Turbo GT Weissach",
    bodyStyle = "Sedan",
    exteriorColor = "Metallic Black",
    interiorColor = "Carbon Black",
    engine = "Dual Permanent-Magnet Synchronous",
    transmission = "2-Speed Automatic",
    drivetrain = "AWD",
    odometerKm = 2285,
    fuelType = "electric",
    conditionGrade = 5.0,
    conditionReport = "Pristine factory condition. Zero track stress or panel scuffs.",
    damageNotes = emptyList(),
    titleStatus = "clean",
    location = VehicleLocation("Austin", "TX"),
    auctionDetails = AuctionDetails(
        auctionStartIso = "2026-10-27T12:00:00Z",
        startingBid = 150000.0,
        reservePrice = 200000.0,
        buyNowPrice = null
    ),
    images = listOf("https://images.unsplash.com/photo-1614162692292-7ac56d7f7f1e?auto=format&fit=crop&w=1200&q=80"),
    sellingDealership = "Apex Performance Center",
    lot = "042",
    currentBid = 185000.0,
    bidCount = 12
)

@Preview(name = "Home Screen - Compact (Dark)", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "Home Screen - Compact (Light)", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(
    name = "Home Screen - Medium (Tablet Portrait)",
    showBackground = true,
    widthDp = 800,
    heightDp = 1280,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Preview(
    name = "Home Screen - Large (Tablet Landscape)",
    showBackground = true,
    widthDp = 1280,
    heightDp = 800,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun HomeScreenSuccessPreview() {
    BlockTheme {
        HomeScreen(
            uiState = HomeUiState.Success(
                vehicles = listOf(
                    sampleVehicle,
                    sampleVehicle.copy(
                        id = "vehicle-sample-2",
                        year = 2025,
                        make = "BMW",
                        model = "M4 Competition",
                        trim = "xDrive",
                        odometerKm = 5120,
                        currentBid = 92000.0,
                        conditionGrade = 4.8
                    ),
                    sampleVehicle.copy(
                        id = "vehicle-sample-3",
                        year = 2023,
                        make = "Mercedes-AMG",
                        model = "GT 63 S",
                        trim = "E Performance",
                        odometerKm = 8400,
                        currentBid = 142000.0,
                        conditionGrade = 4.9
                    )
                ),
                searchQuery = "Porsche Taycan Turbo GT"
            ),
            onEvent = {}
        )
    }
}

@Preview(name = "Home Screen - Loading", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun HomeScreenLoadingPreview() {
    BlockTheme {
        HomeScreen(
            uiState = HomeUiState.Loading,
            onEvent = {}
        )
    }
}

@Preview(name = "Home Screen - Error", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun HomeScreenErrorPreview() {
    BlockTheme {
        HomeScreen(
            uiState = HomeUiState.Error(message = "Unable to connect to telemetry server. Please verify network."),
            onEvent = {}
        )
    }
}

@Preview(name = "Home Screen - Empty", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun HomeScreenEmptyPreview() {
    BlockTheme {
        HomeScreen(
            uiState = HomeUiState.Empty,
            onEvent = {}
        )
    }
}
