package com.note.note.presentation.list.action

sealed interface NoteListAction {
    data class NoteClick(val noteId: Long): NoteListAction
}