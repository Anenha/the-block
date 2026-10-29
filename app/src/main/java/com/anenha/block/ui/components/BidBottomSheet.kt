package com.anenha.block.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
 * Reusable modal bottom sheet allowing buyers to submit a live bid on a vehicle.
 * Encapsulates bid state management, validation against the current leading bid,
 * quick-bid increment chips, and submission feedback.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BidBottomSheet(
    vehicle: Vehicle,
    onDismissRequest: () -> Unit,
    onPlaceBid: (vehicleId: String, amount: Double) -> Unit,
    modifier: Modifier = Modifier,
    isSubmitting: Boolean = false,
    externalErrorMessage: String? = null
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val extendedColors = MaterialTheme.extendedColors

    val currentBid = vehicle.currentBid ?: vehicle.auctionDetails.startingBid
    val defaultNextBid = currentBid + 100.0

    var bidInput by remember(vehicle.id, currentBid) {
        mutableStateOf(defaultNextBid.toLong().toString())
    }

    val parsedAmount = bidInput.toDoubleOrNull()
    val validationError = when {
        bidInput.isBlank() -> stringResource(R.string.error_enter_bid_amount)
        parsedAmount == null -> stringResource(R.string.error_valid_numeric_amount)
        parsedAmount <= currentBid -> stringResource(R.string.error_bid_too_low, formatCurrency(currentBid))
        else -> null
    }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = extendedColors.cardBackground,
        contentColor = extendedColors.textPrimary,
        shape = MaterialTheme.shapes.large,
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .size(width = 36.dp, height = 4.dp)
                    .clip(FullShape)
                    .background(extendedColors.borderSubtle)
            )
        },
        modifier = modifier
    ) {
        BidBottomSheetContent(
            vehicle = vehicle,
            currentBid = currentBid,
            bidInput = bidInput,
            onBidInputChange = { bidInput = it.filter { ch -> ch.isDigit() } },
            onQuickIncrementClick = { increment ->
                val calculated = (currentBid + increment).toLong()
                bidInput = calculated.toString()
            },
            onConfirmClick = {
                parsedAmount?.let { amount ->
                    onPlaceBid(vehicle.id, amount)
                }
            },
            onCancelClick = onDismissRequest,
            validationError = externalErrorMessage ?: validationError,
            isSubmitting = isSubmitting,
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
        )
    }
}

/**
 * Pure presentation composable for the Bid Bottom Sheet content.
 * Accepts immutable state and callbacks, facilitating comprehensive preview testing.
 */
@Composable
fun BidBottomSheetContent(
    vehicle: Vehicle,
    currentBid: Double,
    bidInput: String,
    onBidInputChange: (String) -> Unit,
    onQuickIncrementClick: (Double) -> Unit,
    onConfirmClick: () -> Unit,
    onCancelClick: () -> Unit,
    validationError: String?,
    isSubmitting: Boolean,
    modifier: Modifier = Modifier,
    quickIncrements: List<Double> = listOf(100.0, 250.0, 500.0, 1000.0)
) {
    val extendedColors = MaterialTheme.extendedColors
    val spacing = MaterialTheme.spacing
    val keyboardController = LocalSoftwareKeyboardController.current

    val parsedAmount = bidInput.toDoubleOrNull()
    val isAmountValid = parsedAmount != null && parsedAmount > currentBid && !isSubmitting

    Column(
        modifier = modifier
            .padding(horizontal = spacing.marginMobile)
            .padding(bottom = spacing.spaceMd)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(spacing.spaceMd)
    ) {
        // Sheet Title & Close Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = stringResource(R.string.label_place_bid),
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = extendedColors.performanceCyan
                )
                Text(
                    text = stringResource(R.string.subtitle_live_auction),
                    style = MaterialTheme.typography.headlineSmall,
                    color = extendedColors.textPrimary
                )
            }

            IconButton(onClick = onCancelClick) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(R.string.cd_close),
                    tint = extendedColors.textMuted
                )
            }
        }

        // Vehicle Summary Card
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.surfaceContainerLowest,
            border = BorderStroke(1.dp, extendedColors.borderSubtle),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(spacing.spaceSm),
                horizontalArrangement = Arrangement.spacedBy(spacing.spaceMd),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Thumbnail
                Box(
                    modifier = Modifier
                        .size(width = 80.dp, height = 60.dp)
                        .clip(MaterialTheme.shapes.small)
                        .background(MaterialTheme.colorScheme.surfaceContainerHighest)
                ) {
                    val imageUrl = vehicle.images.firstOrNull()
                    if (!imageUrl.isNullOrBlank()) {
                        SubcomposeAsyncImage(
                            model = imageUrl,
                            contentDescription = vehicle.title,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = vehicle.make.take(2).uppercase(),
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = extendedColors.performanceCyan
                            )
                        }
                    }
                }

                // Title and Lot Info
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        text = "${vehicle.year} ${vehicle.title}",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = extendedColors.textPrimary,
                        maxLines = 1
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(spacing.spaceSm),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        LotNumberBadge(lot = vehicle.lot)
                        Text(
                            text = vehicle.location.formatted,
                            style = MaterialTheme.typography.bodySmall,
                            color = extendedColors.textMuted
                        )
                    }
                }
            }
        }

        // Current Bid Telemetry Readout Box
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.surfaceContainerLowest,
            border = BorderStroke(1.dp, extendedColors.borderSubtle),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(spacing.spaceMd),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(
                        text = if (vehicle.currentBid != null) stringResource(R.string.label_current_highest_bid) else stringResource(R.string.label_starting_bid),
                        style = MaterialTheme.typography.labelSmall,
                        color = extendedColors.textMuted
                    )
                    Text(
                        text = formatCurrency(currentBid),
                        style = MaterialTheme.typography.metricVal,
                        color = extendedColors.textPrimary
                    )
                }

                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(extendedColors.telemetryEmerald)
                        )
                        Text(
                            text = stringResource(R.string.bids_placed_count, vehicle.bidCount),
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium),
                            color = extendedColors.telemetryEmerald
                        )
                    }

                    vehicle.auctionDetails.reservePrice?.let { reserve ->
                        val reserveMet = currentBid >= reserve
                        Text(
                            text = if (reserveMet) stringResource(R.string.reserve_met) else stringResource(R.string.reserve_amount, formatCurrency(reserve)),
                            style = MaterialTheme.typography.labelSmall,
                            color = if (reserveMet) extendedColors.telemetryEmerald else extendedColors.textMuted
                        )
                    }
                }
            }
        }

        // Quick Bid Increment Chips
        Column(verticalArrangement = Arrangement.spacedBy(spacing.spaceXs)) {
            Text(
                text = stringResource(R.string.label_quick_bid_increments),
                style = MaterialTheme.typography.labelSmall,
                color = extendedColors.textMuted
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(spacing.spaceSm)
            ) {
                quickIncrements.forEach { increment ->
                    val isSelected = parsedAmount == (currentBid + increment)
                    Surface(
                        shape = MaterialTheme.shapes.small,
                        color = if (isSelected) extendedColors.performanceCyan else extendedColors.glassBackground,
                        border = BorderStroke(
                            1.dp,
                            if (isSelected) extendedColors.performanceCyan else extendedColors.borderSubtle
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                onQuickIncrementClick(increment)
                            }
                    ) {
                        Box(
                            modifier = Modifier.padding(vertical = spacing.spaceSm),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "+${formatCurrency(increment)}",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else extendedColors.textPrimary
                            )
                        }
                    }
                }
            }
        }

        // Bid Input Field
        Column(verticalArrangement = Arrangement.spacedBy(spacing.spaceXs)) {
            Text(
                text = stringResource(R.string.label_your_bid_amount),
                style = MaterialTheme.typography.labelSmall,
                color = extendedColors.textMuted
            )

            OutlinedTextField(
                value = bidInput,
                onValueChange = onBidInputChange,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                prefix = {
                    Text(
                        text = "$ ",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = extendedColors.performanceCyan
                    )
                },
                trailingIcon = {
                    if (bidInput.isNotEmpty()) {
                        IconButton(onClick = { onBidInputChange("") }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = stringResource(R.string.cd_clear_input),
                                tint = extendedColors.textMuted,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                        if (isAmountValid) {
                            onConfirmClick()
                        }
                    }
                ),
                isError = validationError != null,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = extendedColors.performanceCyan,
                    unfocusedBorderColor = extendedColors.borderSubtle,
                    errorBorderColor = MaterialTheme.colorScheme.error,
                    focusedTextColor = extendedColors.textPrimary,
                    unfocusedTextColor = extendedColors.textPrimary,
                    cursorColor = extendedColors.performanceCyan
                ),
                shape = MaterialTheme.shapes.medium
            )

            if (validationError != null) {
                Text(
                    text = validationError,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(start = spacing.spaceXs)
                )
            } else if (parsedAmount != null && parsedAmount > currentBid) {
                Text(
                    text = stringResource(R.string.hint_new_leading_bid),
                    style = MaterialTheme.typography.bodySmall,
                    color = extendedColors.telemetryEmerald,
                    modifier = Modifier.padding(start = spacing.spaceXs)
                )
            }
        }

        Spacer(modifier = Modifier.height(spacing.spaceXs))

        // Action Buttons
        Column(verticalArrangement = Arrangement.spacedBy(spacing.spaceSm)) {
            Button(
                onClick = {
                    keyboardController?.hide()
                    onConfirmClick()
                },
                enabled = isAmountValid,
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors(
                    containerColor = extendedColors.performanceCyan,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    disabledContainerColor = extendedColors.borderSubtle,
                    disabledContentColor = extendedColors.textMuted
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .shadow(
                        elevation = if (isAmountValid) 8.dp else 0.dp,
                        shape = MaterialTheme.shapes.medium,
                        ambientColor = extendedColors.performanceCyan,
                        spotColor = extendedColors.performanceCyan
                    ),
                contentPadding = PaddingValues(spacing.spaceMd)
            ) {
                if (isSubmitting) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    val label = if (parsedAmount != null && parsedAmount > currentBid) {
                        stringResource(R.string.btn_confirm_bid_amount, formatCurrency(parsedAmount))
                    } else {
                        stringResource(R.string.btn_place_bid)
                    }
                    Text(
                        text = label,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }

            OutlinedButton(
                onClick = onCancelClick,
                shape = MaterialTheme.shapes.medium,
                border = BorderStroke(1.dp, extendedColors.borderSubtle),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text(
                    text = stringResource(R.string.btn_cancel),
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                    color = extendedColors.textMuted
                )
            }
        }
    }
}

// ==========================================
// PREVIEWS
// ==========================================

private fun sampleVehicleForPreview(): Vehicle = Vehicle(
    id = "sample-1",
    vin = "WP0AC2Y18RSA0001",
    year = 2024,
    make = "Porsche",
    model = "Taycan",
    trim = "Turbo GT",
    bodyStyle = "Sedan",
    exteriorColor = "Metallic Black",
    interiorColor = "Carbon Black",
    engine = "Dual Electric Motors",
    transmission = "Automatic",
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
        reservePrice = 190000.0,
        buyNowPrice = null
    ),
    images = emptyList(),
    sellingDealership = "Austin Exotics",
    lot = "A-0102",
    currentBid = 175000.0,
    bidCount = 12
)

@Preview(name = "BidBottomSheet - Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun BidBottomSheetDarkPreview() {
    BlockTheme(darkTheme = true) {
        Surface(color = MaterialTheme.colorScheme.background) {
            BidBottomSheetContent(
                vehicle = sampleVehicleForPreview(),
                currentBid = 175000.0,
                bidInput = "176000",
                onBidInputChange = {},
                onQuickIncrementClick = {},
                onConfirmClick = {},
                onCancelClick = {},
                validationError = null,
                isSubmitting = false
            )
        }
    }
}

@Preview(name = "BidBottomSheet - Validation Error", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun BidBottomSheetErrorPreview() {
    BlockTheme(darkTheme = true) {
        Surface(color = MaterialTheme.colorScheme.background) {
            BidBottomSheetContent(
                vehicle = sampleVehicleForPreview(),
                currentBid = 175000.0,
                bidInput = "170000",
                onBidInputChange = {},
                onQuickIncrementClick = {},
                onConfirmClick = {},
                onCancelClick = {},
                validationError = "Bid must be higher than current bid of $175,000",
                isSubmitting = false
            )
        }
    }
}

@Preview(name = "BidBottomSheet - Light Mode", showBackground = true)
@Composable
private fun BidBottomSheetLightPreview() {
    BlockTheme(darkTheme = false) {
        Surface(color = MaterialTheme.colorScheme.background) {
            BidBottomSheetContent(
                vehicle = sampleVehicleForPreview(),
                currentBid = 175000.0,
                bidInput = "175500",
                onBidInputChange = {},
                onQuickIncrementClick = {},
                onConfirmClick = {},
                onCancelClick = {},
                validationError = null,
                isSubmitting = false
            )
        }
    }
}
