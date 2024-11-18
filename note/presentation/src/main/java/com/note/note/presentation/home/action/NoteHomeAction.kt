package com.note.note.presentation.home.action

sealed interface NoteHomeAction {
    data object BackPressed: NoteHomeAction
    data object WriteNoteClick: NoteHomeAction
    data class NoteClick(val noteId: Long): NoteHomeAction
}