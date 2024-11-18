package com.note.note.presentation.detail.state

import com.note.core.ui.UiText

sealed interface NoteDetailEvent {
    data class Error(val error: UiText) : NoteDetailEvent
    data object DeleteComplete: NoteDetailEvent
}