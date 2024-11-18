package com.note.note.presentation.detail.action

sealed interface NoteDetailAction {
    data object BackPressed: NoteDetailAction
    data object EditNote: NoteDetailAction
    data object DeleteNote: NoteDetailAction

    data class OpenLink(val url: String): NoteDetailAction
    data class ViewImage(val uri: String): NoteDetailAction
}