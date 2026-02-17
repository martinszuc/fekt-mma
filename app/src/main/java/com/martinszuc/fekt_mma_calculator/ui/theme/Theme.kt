package com.martinszuc.fekt_mma_calculator.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val CalculatorDarkColorScheme = darkColorScheme(
    background = BackgroundDark,
    surface = BackgroundDark,
    surfaceVariant = SurfaceVariantDark,
    secondaryContainer = SecondaryContainerDark,
    primary = PrimaryAccent,
    onBackground = OnBackgroundDark,
    onSurface = OnBackgroundDark,
    onSurfaceVariant = OnSurfaceVariantDark
)

@Composable
fun FektmmacalculatorTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = CalculatorDarkColorScheme,
        typography = Typography,
        content = content
    )
}
