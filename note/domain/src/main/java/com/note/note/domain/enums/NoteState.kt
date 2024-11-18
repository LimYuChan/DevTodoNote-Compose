package com.note.note.domain.enums

enum class NoteState(val code: Int) {
    TODO(code = 1),
    DONE(code = 2)
}

fun Int.toNoteState(): NoteState? {
    return NoteState.entries.find { it.code == this }
}