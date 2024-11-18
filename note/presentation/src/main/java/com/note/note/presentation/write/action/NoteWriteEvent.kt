package com.note.note.presentation.write.action

import com.note.core.ui.UiText

sealed interface NoteWriteEvent {
    data class Error(val error: UiText): NoteWriteEvent
    data object SaveComplete: NoteWriteEvent
}