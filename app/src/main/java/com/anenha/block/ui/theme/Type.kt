package com.anenha.block.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp

private val fontProvider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = androidx.compose.ui.text.googlefonts.R.array.com_google_android_gms_fonts_certs
)

val SpaceGroteskGoogleFont = GoogleFont("Space Grotesk")
val OutfitGoogleFont = GoogleFont("Outfit")

val SpaceGroteskFontFamily = FontFamily(
    Font(googleFont = SpaceGroteskGoogleFont, fontProvider = fontProvider, weight = FontWeight.Normal),
    Font(googleFont = SpaceGroteskGoogleFont, fontProvider = fontProvider, weight = FontWeight.Medium),
    Font(googleFont = SpaceGroteskGoogleFont, fontProvider = fontProvider, weight = FontWeight.SemiBold),
    Font(googleFont = SpaceGroteskGoogleFont, fontProvider = fontProvider, weight = FontWeight.Bold)
)

val OutfitFontFamily = FontFamily(
    Font(googleFont = OutfitGoogleFont, fontProvider = fontProvider, weight = FontWeight.Normal),
    Font(googleFont = OutfitGoogleFont, fontProvider = fontProvider, weight = FontWeight.Medium),
    Font(googleFont = OutfitGoogleFont, fontProvider = fontProvider, weight = FontWeight.SemiBold),
    Font(googleFont = OutfitGoogleFont, fontProvider = fontProvider, weight = FontWeight.Bold)
)

val Typography = Typography(
    // display-lg: Space Grotesk 44px / 700 / 52px / -0.03em
    displayLarge = TextStyle(
        fontFamily = SpaceGroteskFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 44.sp,
        lineHeight = 52.sp,
        letterSpacing = (-0.03).em
    ),
    // display-lg-mobile: Space Grotesk 34px / 700 / 40px / -0.02em
    displayMedium = TextStyle(
        fontFamily = SpaceGroteskFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 34.sp,
        lineHeight = 40.sp,
        letterSpacing = (-0.02).em
    ),
    displaySmall = TextStyle(
        fontFamily = SpaceGroteskFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 28.sp,
        lineHeight = 34.sp,
        letterSpacing = (-0.02).em
    ),
    // headline-lg: Space Grotesk 32px / 600 / 38px / -0.02em
    headlineLarge = TextStyle(
        fontFamily = SpaceGroteskFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 32.sp,
        lineHeight = 38.sp,
        letterSpacing = (-0.02).em
    ),
    // headline-md: Space Grotesk 22px / 600 / 28px / -0.01em
    headlineMedium = TextStyle(
        fontFamily = SpaceGroteskFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = (-0.01).em
    ),
    // headline-sm: Space Grotesk 18px / 600 / 24px
    headlineSmall = TextStyle(
        fontFamily = SpaceGroteskFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
        lineHeight = 24.sp
    ),
    titleLarge = TextStyle(
        fontFamily = SpaceGroteskFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        lineHeight = 26.sp
    ),
    titleMedium = TextStyle(
        fontFamily = SpaceGroteskFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 22.sp
    ),
    titleSmall = TextStyle(
        fontFamily = SpaceGroteskFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp
    ),
    // body-lg: Outfit 18px / 400 / 28px
    bodyLarge = TextStyle(
        fontFamily = OutfitFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        lineHeight = 28.sp
    ),
    // body-md: Outfit 15px / 400 / 22px
    bodyMedium = TextStyle(
        fontFamily = OutfitFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 15.sp,
        lineHeight = 22.sp
    ),
    // body-sm: Outfit 13px / 400 / 18px
    bodySmall = TextStyle(
        fontFamily = OutfitFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp,
        lineHeight = 18.sp
    ),
    // label-lg: Space Grotesk 14px / 600 / 18px / 0.04em
    labelLarge = TextStyle(
        fontFamily = SpaceGroteskFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 18.sp,
        letterSpacing = 0.04.em
    ),
    // label-md: Space Grotesk 12px / 500 / 16px / 0.05em
    labelMedium = TextStyle(
        fontFamily = SpaceGroteskFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.05.em
    ),
    // label-sm: Space Grotesk 10px / 600 / 14px / 0.08em
    labelSmall = TextStyle(
        fontFamily = SpaceGroteskFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 10.sp,
        lineHeight = 14.sp,
        letterSpacing = 0.08.em
    )
)

// Telemetry metric value readout: Space Grotesk 24px / 700 / 28px / -0.02em
val Typography.metricVal: TextStyle
    get() = TextStyle(
        fontFamily = SpaceGroteskFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        lineHeight = 28.sp,
        letterSpacing = (-0.02).em
    )

// Mobile variations
val Typography.displayLargeMobile: TextStyle
    get() = TextStyle(
        fontFamily = SpaceGroteskFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 34.sp,
        lineHeight = 40.sp,
        letterSpacing = (-0.02).em
    )

val Typography.headlineLargeMobile: TextStyle
    get() = TextStyle(
        fontFamily = SpaceGroteskFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 26.sp,
        lineHeight = 32.sp,
        letterSpacing = (-0.01).em
    )