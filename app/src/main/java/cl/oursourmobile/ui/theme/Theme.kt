
package cl.oursourmobile.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val OurSourLightColors = lightColorScheme(
    primary = SourGreen,
    onPrimary = SourWhite,
    primaryContainer = SourGreenLight,
    onPrimaryContainer = SourWhite,

    secondary = SourGold,
    onSecondary = SourGreenDark,
    secondaryContainer = SourGoldLight,
    onSecondaryContainer = SourGreenDark,

    tertiary = SourGreenLight,
    onTertiary = SourWhite,

    background = SourIvory,
    onBackground = SourText,

    surface = SourWhite,
    onSurface = SourText,
    surfaceVariant = SourSand,
    onSurfaceVariant = SourTextSecondary,

    outline = SourTextSecondary,
    error = SourError,
    onError = SourWhite
)

private val OurSourDarkColors = darkColorScheme(
    primary = SourGold,
    onPrimary = SourGreenDark,
    primaryContainer = SourGreenLight,
    onPrimaryContainer = SourWhite,

    secondary = SourGoldLight,
    onSecondary = SourGreenDark,

    tertiary = SourGold,
    onTertiary = SourGreenDark,

    background = SourGreenDark,
    onBackground = SourIvory,

    surface = SourGreen,
    onSurface = SourIvory,
    surfaceVariant = SourGreenLight,
    onSurfaceVariant = SourSand,

    outline = SourSand,
    error = SourError,
    onError = SourWhite
)

@Composable
fun OurSourMobileTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        OurSourDarkColors
    } else {
        OurSourLightColors
    }

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}
