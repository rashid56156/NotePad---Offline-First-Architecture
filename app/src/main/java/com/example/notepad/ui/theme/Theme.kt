package com.example.notepad.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary = androidx.compose.ui.graphics.Color(0xFF1A73E8),
    onPrimary = androidx.compose.ui.graphics.Color.White,
    primaryContainer = androidx.compose.ui.graphics.Color(0xFFD3E3FD),
    onPrimaryContainer = androidx.compose.ui.graphics.Color(0xFF041E49),
    secondary = androidx.compose.ui.graphics.Color(0xFF00639B),
    surface = androidx.compose.ui.graphics.Color(0xFFFAFAFA),
    surfaceVariant = androidx.compose.ui.graphics.Color(0xFFF1F3F4),
    background = androidx.compose.ui.graphics.Color(0xFFFFFFFF),
    error = androidx.compose.ui.graphics.Color(0xFFB3261E),
)

private val DarkColorScheme = darkColorScheme(
    primary = androidx.compose.ui.graphics.Color(0xFF8ABFFF),
    onPrimary = androidx.compose.ui.graphics.Color(0xFF003064),
    primaryContainer = androidx.compose.ui.graphics.Color(0xFF00468C),
    onPrimaryContainer = androidx.compose.ui.graphics.Color(0xFFD3E3FD),
    surface = androidx.compose.ui.graphics.Color(0xFF1C1B1F),
    surfaceVariant = androidx.compose.ui.graphics.Color(0xFF2B2930),
    background = androidx.compose.ui.graphics.Color(0xFF1C1B1F),
    error = androidx.compose.ui.graphics.Color(0xFFF2B8B5),
)

@Composable
fun NotepadTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
