package com.anenha.block

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.anenha.block.ui.theme.BlockTheme
import com.anenha.block.ui.theme.FullShape
import com.anenha.block.ui.theme.extendedColors
import com.anenha.block.ui.theme.metricVal
import com.anenha.block.ui.theme.spacing
import com.anenha.block.ui.navigation.BlockNavHost
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BlockTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    BlockNavHost(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun ThemeShowcase(modifier: Modifier = Modifier) {
    val extendedColors = MaterialTheme.extendedColors
    val spacing = MaterialTheme.spacing

    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = spacing.marginMobile, vertical = spacing.spaceMd)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(spacing.spaceMd)
    ) {
        // Brand Header
        Column(verticalArrangement = Arrangement.spacedBy(spacing.spaceXs)) {
            Text(
                text = "THE BLOCK",
                style = MaterialTheme.typography.labelSmall,
                color = extendedColors.performanceCyan
            )
            Text(
                text = "Kinetic Apex",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "High-performance vehicle auction platform telemetry & design system.",
                style = MaterialTheme.typography.bodyMedium,
                color = extendedColors.textSecondary
            )
        }

        // Vehicle Telemetry Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = extendedColors.borderSubtle,
                    shape = MaterialTheme.shapes.small
                ),
            shape = MaterialTheme.shapes.small,
            colors = CardDefaults.cardColors(
                containerColor = extendedColors.cardBackground
            )
        ) {
            Column(
                modifier = Modifier.padding(spacing.spaceMd),
                verticalArrangement = Arrangement.spacedBy(spacing.spaceSm)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "APEX GT 2026",
                        style = MaterialTheme.typography.headlineMedium,
                        color = extendedColors.textPrimary
                    )

                    // Track Tag
                    Box(
                        modifier = Modifier
                            .clip(FullShape)
                            .background(extendedColors.trackAmber.copy(alpha = 0.15f))
                            .padding(horizontal = spacing.spaceSm, vertical = spacing.spaceXs)
                    ) {
                        Text(
                            text = "TRACK PACK",
                            style = MaterialTheme.typography.labelSmall,
                            color = extendedColors.trackAmber
                        )
                    }
                }

                Text(
                    text = "Dual-motor AWD with active aerodynamic trim and carbon-ceramic telemetry cluster.",
                    style = MaterialTheme.typography.bodySmall,
                    color = extendedColors.textSecondary
                )

                Spacer(modifier = Modifier.height(spacing.spaceXs))

                // Telemetry Cluster
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "0-60 MPH",
                            style = MaterialTheme.typography.labelSmall,
                            color = extendedColors.textMuted
                        )
                        Text(
                            text = "2.1s",
                            style = MaterialTheme.typography.metricVal,
                            color = extendedColors.textPrimary
                        )
                    }

                    Column {
                        Text(
                            text = "TOP SPEED",
                            style = MaterialTheme.typography.labelSmall,
                            color = extendedColors.textMuted
                        )
                        Text(
                            text = "205 MPH",
                            style = MaterialTheme.typography.metricVal,
                            color = extendedColors.textPrimary
                        )
                    }

                    Column {
                        Text(
                            text = "BATTERY / SYNC",
                            style = MaterialTheme.typography.labelSmall,
                            color = extendedColors.textMuted
                        )
                        Text(
                            text = "98%",
                            style = MaterialTheme.typography.metricVal,
                            color = extendedColors.telemetryEmerald
                        )
                    }
                }

                Spacer(modifier = Modifier.height(spacing.spaceXs))

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(spacing.spaceSm)
                ) {
                    Button(
                        onClick = {},
                        modifier = Modifier.weight(1f),
                        shape = MaterialTheme.shapes.small,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = extendedColors.performanceCyan,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Text(
                            text = "Place Bid",
                            style = MaterialTheme.typography.labelLarge
                        )
                    }

                    OutlinedButton(
                        onClick = {},
                        modifier = Modifier.weight(1f),
                        shape = MaterialTheme.shapes.small
                    ) {
                        Text(
                            text = "View Specs",
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
            }
        }

        // Live VehicleCard Component Showcase
        Text(
            text = "AUCTION INVENTORY COMPONENT",
            style = MaterialTheme.typography.labelSmall,
            color = extendedColors.performanceCyan
        )

        com.anenha.block.ui.components.VehicleCard(
            imageUrl = "https://images.unsplash.com/photo-1614162692292-7ac56d7f7f1e",
            year = 2024,
            title = "Taycan Turbo GT Weissach",
            odometerValue = "1,420",
            odometerUnit = "mi",
            location = "Austin, TX",
            currentBid = 185000.0,
            reservePrice = 200000.0,
            conditionGrade = 5.0,
            titleStatus = "clean"
        )
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Composable
fun LightModePreview() {
    BlockTheme(darkTheme = false) {
        Surface {
            ThemeShowcase()
        }
    }
}

@Preview(name = "Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun DarkModePreview() {
    BlockTheme(darkTheme = true) {
        Surface {
            ThemeShowcase()
        }
    }
}