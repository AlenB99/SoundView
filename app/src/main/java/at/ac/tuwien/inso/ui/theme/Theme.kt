package at.ac.tuwien.inso.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(
    primary = Orange200,
    primaryVariant = Orange200,
    secondary = Orange500,
    error = ColorError
)

private val LightColorPalette = lightColors(
    primary = Orange500,
    primaryVariant = Orange500,
    secondary = Orange200,
    error = ColorError
)

@Composable
fun AndroidArchitectureExampleTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
