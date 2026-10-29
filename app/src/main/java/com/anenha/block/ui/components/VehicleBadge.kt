package com.anenha.block.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.anenha.block.R
import com.anenha.block.ui.theme.BlockTheme
import com.anenha.block.ui.theme.FullShape
import com.anenha.block.ui.theme.extendedColors
import com.anenha.block.ui.theme.spacing

/**
 * Type descriptor for reusable vehicle badges.
 */
sealed interface VehicleBadgeType {
    data class Condition(val grade: Double) : VehicleBadgeType
    data class TitleStatus(val status: String) : VehicleBadgeType
    data class Lot(val lotNumber: String) : VehicleBadgeType
    data class LiveAuction(val lotCount: Int) : VehicleBadgeType
}

/**
 * Core reusable badge component rendering a standardized pill with optional leading indicator.
 */
@Composable
fun VehicleBadge(
    type: VehicleBadgeType,
    modifier: Modifier = Modifier
) {
    val extendedColors = MaterialTheme.extendedColors
    val spacing = MaterialTheme.spacing

    when (type) {
        is VehicleBadgeType.Condition -> {
            Surface(
                shape = FullShape,
                color = extendedColors.cyanCore,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = modifier
            ) {
                Text(
                    text = stringResource(R.string.badge_grade, type.grade.toString()),
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.padding(
                        horizontal = spacing.spaceSm,
                        vertical = spacing.spaceXs
                    )
                )
            }
        }

        is VehicleBadgeType.TitleStatus -> {
            Surface(
                shape = FullShape,
                color = MaterialTheme.colorScheme.surfaceContainerHigh,
                modifier = modifier
            ) {
                Text(
                    text = type.status.uppercase(),
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = extendedColors.textSecondary,
                    modifier = Modifier.padding(
                        horizontal = spacing.spaceSm,
                        vertical = spacing.spaceXs
                    )
                )
            }
        }

        is VehicleBadgeType.Lot -> {
            Surface(
                shape = FullShape,
                color = Color.White.copy(alpha = 0.95f),
                shadowElevation = 2.dp,
                modifier = modifier
            ) {
                Row(
                    modifier = Modifier.padding(
                        horizontal = spacing.spaceMd,
                        vertical = spacing.spaceXs
                    ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(spacing.spaceSm)
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(extendedColors.cyanCore)
                    )
                    Text(
                        text = stringResource(R.string.badge_lot_number, type.lotNumber),
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = Color(0xFF0F3260)
                    )
                }
            }
        }

        is VehicleBadgeType.LiveAuction -> {
            Surface(
                shape = FullShape,
                color = extendedColors.glassBackground,
                border = BorderStroke(1.dp, extendedColors.borderSubtle),
                modifier = modifier
            ) {
                Row(
                    modifier = Modifier.padding(
                        horizontal = spacing.spaceMd,
                        vertical = spacing.spaceSm
                    ),
                    horizontalArrangement = Arrangement.spacedBy(spacing.spaceSm),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(extendedColors.telemetryEmerald)
                    )
                    Text(
                        text = stringResource(R.string.badge_lots_active, type.lotCount),
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = extendedColors.telemetryEmerald
                    )
                }
            }
        }
    }
}

/**
 * Condition grade pill badge (e.g., "GRADE 4.8").
 * Uses cyanCore background with onPrimaryContainer text.
 */
@Composable
fun ConditionGradeBadge(
    grade: Double,
    modifier: Modifier = Modifier
) {
    VehicleBadge(type = VehicleBadgeType.Condition(grade), modifier = modifier)
}

/**
 * Title status pill badge (e.g., "CLEAN", "SALVAGE").
 * Uses surfaceContainerHigh background with textSecondary text.
 */
@Composable
fun TitleStatusBadge(
    status: String,
    modifier: Modifier = Modifier
) {
    VehicleBadge(type = VehicleBadgeType.TitleStatus(status), modifier = modifier)
}

/**
 * Floating Lot number badge with cyan dot indicator (e.g., "LOT # A-0001").
 */
@Composable
fun LotNumberBadge(
    lot: String,
    modifier: Modifier = Modifier
) {
    VehicleBadge(type = VehicleBadgeType.Lot(lot), modifier = modifier)
}

/**
 * Live auction telemetry status badge with pulsating green dot (e.g., "24 LOTS ACTIVE").
 */
@Composable
fun LiveAuctionBadge(
    lotCount: Int,
    modifier: Modifier = Modifier
) {
    VehicleBadge(type = VehicleBadgeType.LiveAuction(lotCount), modifier = modifier)
}

/**
 * Convenience layout displaying condition grade and title status side by side.
 */
@Composable
fun VehicleBadgesRow(
    conditionGrade: Double?,
    titleStatus: String?,
    modifier: Modifier = Modifier,
    spacing: Dp = MaterialTheme.spacing.spaceSm
) {
    if (conditionGrade != null || !titleStatus.isNullOrBlank()) {
        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.spacedBy(spacing),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (conditionGrade != null) {
                ConditionGradeBadge(grade = conditionGrade)
            }
            if (!titleStatus.isNullOrBlank()) {
                TitleStatusBadge(status = titleStatus)
            }
        }
    }
}

// ---------------------------------------------------------------------------
// Previews
// ---------------------------------------------------------------------------

@Preview(name = "Badges - Light Mode", showBackground = true)
@Preview(name = "Badges - Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun VehicleBadgesPreview() {
    BlockTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            VehicleBadgesRow(
                conditionGrade = 4.8,
                titleStatus = "Clean"
            )

            LotNumberBadge(lot = "A-0001")

            LiveAuctionBadge(lotCount = 18)
        }
    }
}
