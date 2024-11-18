package com.note.auth.presentation.action

import android.net.Uri
import com.note.core.ui.UiText

sealed interface AuthEvent {
    data class LaunchLoginUri(val uri: Uri): AuthEvent
    data class Error(val error: UiText): AuthEvent
}