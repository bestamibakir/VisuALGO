package com.bestamibakir.visualgo.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val darkColorScheme = darkColorScheme(
    primary = Color(0xFF82B1FF),          // Daha açık mavi
    onPrimary = Color.Black,
    primaryContainer = Color(0xFF0D47A1), // Koyu mavi
    secondary = Color(0xFFFFAB40),        // Açık turuncu
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFF8C4A00),
    tertiary = Color(0xFF81C784),         // Açık yeşil
    surface = Color(0xFF121212),
    surfaceVariant = Color(0xFF212121),   // Koyu gri
    background = Color(0xFF121212),
    // Diğer renkler...
)

private val lightColorScheme = lightColorScheme(
    primary = Color(0xFF2962FF),          // Canlı mavi
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE3F2FD), // Açık mavi
    secondary = Color(0xFFFF6D00),        // Turuncu
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFFE0B2),
    tertiary = Color(0xFF4CAF50),         // Yeşil
    surface = Color.White,
    surfaceVariant = Color(0xFFF5F5F5),   // Açık gri
    background = Color.White,
    // Diğer renkler...
)

@Composable
fun VisuALGOTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> darkColorScheme
        else -> lightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}