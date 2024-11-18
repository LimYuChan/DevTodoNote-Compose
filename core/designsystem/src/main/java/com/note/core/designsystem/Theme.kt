package com.note.core.designsystem

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

val DarkColorScheme = darkColorScheme(
    primary = NotePrimary,
    background = NoteBlack,
    surface = NoteDarkGray,
    secondary = NoteWhite,
    tertiary = NoteWhite,
    primaryContainer = NotePrimary30,
    onPrimary = NoteWhite,
    onBackground = NoteWhite,
    onSurface = NoteWhite,
    onSurfaceVariant = NoteGray,
    error = NoteError,
    errorContainer = NoteError5
)

@Composable
fun NoteTheme(
    content: @Composable () -> Unit
) {
    val view = LocalView.current

    if(!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}